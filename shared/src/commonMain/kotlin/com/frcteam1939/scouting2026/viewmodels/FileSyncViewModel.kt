package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class FileSyncViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository,
) : ViewModel() {
    private val settings: StateFlow<AppSettings?> = settingsRepository.settings

    suspend fun getPitDataForExport(): String {
        val eventName = settings.value?.currentEvent ?: ""
        val teamList = eventsRepository.getEventByName(eventName).first()?.eventTeamList ?: emptyList()

        val pitDataList: List<Pair<String, String>> = teamList.map { team ->
            val pitData = scoutedDataRepository.getPitData(team, "OKC Pit Form 2026", eventName).first()
            val teamName = pitData?.team ?: ""
            val scoutedData = pitData?.scoutedData ?: ""
            teamName to scoutedData
        }

        return pitDataList.joinToString("\n") { (team, data) ->
            "$team,$data"
        }
    }

    suspend fun getMatchDataForExport(): String {
        val eventName = settings.value?.currentEvent ?: ""
        val matchData = scoutedDataRepository.getMatchData("OKC Match Form 2026", eventName).first()

        return matchData.joinToString("\n") { data ->
            "${data.team},${data.matchNumber},${data.scoutedData}"
        }
    }

    fun importPitData(importedDataString: String) {
        val eventName = settings.value?.currentEvent ?: ""
        val lines = importedDataString.lines().filter { it.isNotBlank() }

        lines.forEach { line ->
            val firstCommaIndex = line.indexOf(",")

            if (firstCommaIndex > 0) {
                val team = line.substring(0, firstCommaIndex).trim()
                val scoutedData = line.substring(firstCommaIndex + 1).trim()

                if (team.isNotBlank()) {
                    scoutedDataRepository.insertPitData(
                        scoutedData = ScoutedData(
                            id = 0,
                            eventName = eventName,
                            team = team,
                            matchNumber = "0",
                            formName = "OKC Pit Form 2026",
                            formType = "pit",
                            scoutedData = scoutedData
                        ),
                        eventName = eventName
                    )
                }
            }
        }
    }

    suspend fun importMatchData(importedDataString: String) {
        val eventName = settings.value?.currentEvent ?: ""
        val lines = importedDataString.lines().filter { it.isNotBlank() }

        lines.forEach { line ->
            val firstCommaIndex = line.indexOf(",")
            val secondCommaIndex = if (firstCommaIndex != -1) line.indexOf(",", firstCommaIndex + 1) else -1

            if (firstCommaIndex != -1 && secondCommaIndex != -1) {
                val team = line.substring(0, firstCommaIndex).trim()
                val matchNumber = line.substring(firstCommaIndex + 1, secondCommaIndex).trim()
                val scoutedData = line.substring(secondCommaIndex + 1).trim()

                if (team.isNotBlank() && matchNumber.isNotBlank()) {
                    if (scoutedDataRepository.getMatchByTeamAndNumber(team, matchNumber.toInt(), "OKC Match Form 2026", eventName).first() == null) {
                        scoutedDataRepository.insertMatchData(
                            ScoutedData(
                                id = 0,
                                eventName = eventName,
                                team = team,
                                matchNumber = matchNumber,
                                formName = "OKC Match Form 2026",
                                formType = "match",
                                scoutedData = scoutedData
                            )
                        )
                    } else {
                        println("Match data already exists for team $team and match number $matchNumber, so not submitted!")
                    }
                }
            }
        }
    }
}
