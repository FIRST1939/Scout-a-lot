package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcteam1939.scouting2026.data.dao.SettingsDao
import com.frcteam1939.scouting2026.data.entities.AppSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.Boolean

class SettingsViewModel(private val dao: SettingsDao) : ViewModel() {
    init {
        val defaultSettings = AppSettings(
            exampleSetting = false,
            deviceRobotAssignment = "Red 1",
            currentEvent = ""
        )

        // Set default settings if they don't exist (when app is opened for the first time)
        viewModelScope.launch {
            if (dao.getSettings().first() == null) {
                dao.saveSettings(defaultSettings)
            }
        }
    }

    val settings: StateFlow<AppSettings?> = dao.getSettings()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun updateExampleSetting(enabled: Boolean) {
        viewModelScope.launch {
            settings.value?.let {
                dao.saveSettings(it.copy(exampleSetting = enabled))
            }
        }
    }

    fun updateDeviceRobotAssignment(assignment: String) {
        viewModelScope.launch {
            settings.value?.let {
                dao.saveSettings(it.copy(deviceRobotAssignment = assignment))
            }
        }
    }

    fun updateCurrentEvent(eventName: String) {
        viewModelScope.launch {
            settings.value?.let {
                dao.saveSettings(it.copy(currentEvent = eventName))
            }
        }
    }
}