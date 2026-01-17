package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.dao.SettingsDao
import com.frcteam1939.scouting2026.data.entities.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsRepository(private val dao: SettingsDao, private val viewModelScope: CoroutineScope) {
    init {
        viewModelScope.launch {
            if (dao.getSettings().first() == null) {
                restoreDefaultSettings()
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

    suspend fun restoreDefaultSettings() {
        val defaultSettings = AppSettings(
            exampleSetting = false,
            deviceRobotAssignment = "Red 1",
            currentEvent = ""
        )

        dao.saveSettings(defaultSettings)
    }
}