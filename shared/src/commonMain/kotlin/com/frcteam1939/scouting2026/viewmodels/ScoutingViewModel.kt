package com.frcteam1939.scouting2026.viewmodels

// android ViewModel import can be replaced with a general ViewModel class later, im too lazy
import androidx.lifecycle.ViewModel

import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
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

    suspend fun getCurrentForm(currentEvent: String, formType: String): String {
        val event = eventsRepository.getEventByName(currentEvent).first() ?: return "None Selected"
        return when (formType) {
            "pit" -> event.pitForm.ifBlank { "None Selected" }
            "match" -> event.matchForm.ifBlank { "None Selected" }
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