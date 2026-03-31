package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.assignPitTeams
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlin.collections.emptyList

class ChecklistViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings

    fun getCurrentEvent(): String {
        return settings.value?.currentEvent ?: ""
    }

    var matchInfo = ""
    var teams = emptyList<String>()
    private var _pitScoutingAssignments = MutableStateFlow<List<Pair<String, List<String>>>>(emptyList())
    val pitScoutingAssignments = _pitScoutingAssignments.asStateFlow()
    fun changePitScoutingAssignments(newPitScoutingAssignments: List<Pair<String, List<String>>>) {
        _pitScoutingAssignments.value = newPitScoutingAssignments
    }

    private var _pitScoutedTeams = MutableStateFlow<List<String>>(emptyList())
    val pitScoutedTeams = _pitScoutedTeams.asStateFlow()
    fun changePitScoutedTeams(newPitScoutedTeams: List<String>) {
        _pitScoutedTeams.value = newPitScoutedTeams
    }


    suspend fun loadChecklist() {
        val event = eventsRepository.getEventByName(getCurrentEvent()).first()
        matchInfo = event?.matchInfo ?: ""
        teams = event?.eventTeamList ?: emptyList()

        changePitScoutedTeams(
            teams.filter { team ->
                scoutedDataRepository.getPitData(team, "OKC Pit Form 2026", getCurrentEvent())
                    .firstOrNull() != null
            }
        )

        val deviceRobotAssignments = listOf("Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3")

        changePitScoutingAssignments(
            deviceRobotAssignments.map { pos ->
                pos to assignPitTeams(teams, pos)
            }
        )
    }
}