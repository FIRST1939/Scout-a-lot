package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.DataDependencyContainer.tbaInfo
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.entities.Event
import com.frcteam1939.scouting2026.data.repositories.EventCodesRepository
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import com.frcteam1939.scouting2026.tba.models.TBAMatch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlin.Boolean

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val eventCodesRepository: EventCodesRepository
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings

    fun updatePlayoffsActive(enabled: Boolean) {
        settingsRepository.updatePlayoffsActive(enabled)
    }

    fun updateDeviceRobotAssignment(assignment: String) {
        settingsRepository.updateDeviceRobotAssignment(assignment)
    }

    fun updateCurrentEvent(customEventName: String) {
        settingsRepository.updateCurrentEvent(customEventName)
    }

    fun getEventNames(): Flow<List<String>> {
        return eventsRepository.getEventNames()
    }

    suspend fun getEventByName(name: String): Event? {
        return eventsRepository.getEventByName(name).first()
    }

    suspend fun checkEventValidity(code: String, year: Int): Boolean? {
        if (year >= 1992 && year <= 2026) {
            val eventCodes = eventCodesRepository.getEventCodes(year).first()?.eventCodes
            return eventCodes?.contains(code)
            // returns null if the event codes for that year don't exist
        }
        else {
            return false
        }
    }

    suspend fun createEvent(name: String, code: String, year: Int) {
        val matchInfo = tbaInfo.pullEventMatches("$year$code")
        eventsRepository.createEvent(Event(id = 0, customEventName = name, eventCode = code, eventYear = year, matchInfo = matchInfo))
    }

    fun deleteEvent(name: String) {
        eventsRepository.deleteEvent(name)
    }

    suspend fun updateEventMatchInfo(name: String, code: String, year: Int) {
        val matchInfo = tbaInfo.pullEventMatches("$year$code")
        eventsRepository.updateEventMatchInfo(name, matchInfo)
    }

    suspend fun checkQualsMatchLoadStatus(name: String): Boolean {
        val jsonString = eventsRepository.getEventByName(name).first()?.matchInfo
        val matchInfo = Json.decodeFromString<List<TBAMatch>>(jsonString ?: "")
        for (match in matchInfo) {
            if (match.compLevel == "qm") {
                return true
            }
        }
        return false
    }

    suspend fun checkPlayoffMatchLoadStatus(name: String): Boolean {
        val jsonString = eventsRepository.getEventByName(name).first()?.matchInfo
        val matchInfo = Json.decodeFromString<List<TBAMatch>>(jsonString ?: "")
        for (match in matchInfo) {
            //TODO: add ALL playoff match types
            if (match.compLevel == "f" || match.compLevel == "sf" || match.compLevel == "qf") {
                return true
            }
        }
        return false
    }
}