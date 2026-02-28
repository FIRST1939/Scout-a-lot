package com.frcteam1939.scouting2026.viewmodels

// android ViewModel import can be replaced with a general ViewModel class later, im too lazy
import androidx.lifecycle.ViewModel

import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import com.frcteam1939.scouting2026.showAlert
import com.frcteam1939.scouting2026.tba.models.TBAMatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class ScoutingViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository,
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings
    val alliance: String?
        get() = settings.value?.deviceRobotAssignment?.dropLast(2)
    val robotNumber: Int?
        get() = settings.value?.deviceRobotAssignment?.takeLast(1)?.toIntOrNull()
    private val _matchNumber = MutableStateFlow("")
    val matchNumber: StateFlow<String> = _matchNumber.asStateFlow()

    fun changeMatchNumber(newMatchNumber: String) {
        _matchNumber.value = newMatchNumber
    }

    private val _selectedTeam = MutableStateFlow("")
    val selectedTeam: StateFlow<String> = _selectedTeam.asStateFlow()
    fun changeSelectedTeam(newTeam: String) {
        _selectedTeam.value = newTeam
    }

    suspend fun getTeamList(): List<String> {
        val deviceName = settings.value?.deviceRobotAssignment
        var deviceTag = 0
        val eventName = settings.value?.currentEvent
        if (eventName == null || eventName == "") {
            showAlert("Error getting event teams:", "Current event not found. Make sure you select an event!")
            return emptyList()
        }
        var teamList = eventsRepository.getEventByName(eventName).first()?.eventTeamList ?: emptyList()
        //sorts the teamList to make it consistent and smallest to largest
        teamList = teamList.mapNotNull { it.toIntOrNull() }.sorted().map { it.toString() }
        val deviceTeamList = mutableListOf<String>()
        when(deviceName) {
            "Red 1" -> deviceTag = 0
            "Red 2" -> deviceTag = 1
            "Red 3" -> deviceTag = 2
            "Blue 1" -> deviceTag = 3
            "Blue 2" -> deviceTag = 4
            "Blue 3" -> deviceTag = 5
        }
        while (deviceTag < teamList.size) {
            deviceTeamList.add(teamList[deviceTag])
            deviceTag += 6
        }
        return (deviceTeamList)
    }

    suspend fun getCurrentForm(currentEvent: String, formType: String): String {
        val event = eventsRepository.getEventByName(currentEvent).first() ?: return "None Selected"
        return when (formType) {
            "pit" -> event.pitForm
            "match" -> event.matchForm
            else -> "ERROR: Invalid form type"
        }
    }

    suspend fun getCurrentTeam(): String {
        val event = eventsRepository.getEventByName(settings.value?.currentEvent ?: "")
        val jsonString = event.first()?.matchInfo
        val matches = Json.decodeFromString<List<TBAMatch>>(jsonString ?: "")

        val currentAlliance = this.alliance ?: return "ERROR: COULD NOT FIND TEAM"
        val currentRobotNumber = this.robotNumber ?: return "ERROR: COULD NOT FIND TEAM"

        for (match in matches) {
            val isPlayoff = match.compLevel != "qm"

            // check if the match is the one we're looking for: type AND number
            if (isPlayoff == (settings.value?.playoffsActive ?: false) && match.matchNumber.toString() == matchNumber.value) {
                if (currentAlliance == "Red") {
                    return match.alliances.red.teamKeys[currentRobotNumber - 1].substring(3)
                }
                if (currentAlliance == "Blue") {
                    return match.alliances.blue.teamKeys[currentRobotNumber - 1].substring(3)
                }
            }
        }
        return "ERROR: COULD NOT FIND TEAM"
    }
}