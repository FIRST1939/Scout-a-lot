package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.Boolean

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings

    fun updateExampleSetting(enabled: Boolean) {
        settingsRepository.updateExampleSetting(enabled)
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
}