package com.frcteam1939.scouting2026.feature.settings

import com.frcteam1939.scouting2026.core.database.AppDatabase
import com.frcteam1939.scouting2026.core.database.entity.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsRepository(private val db: AppDatabase, private val applicationScope: CoroutineScope) {
    private val dao = db.settingsDao()

    init {
        applicationScope.launch {
            if (dao.getSettings().first() == null) {
                restoreDefaultSettings()
            }
        }
    }

    val settings: StateFlow<AppSettings?> = dao.getSettings()
        .stateIn(applicationScope, SharingStarted.Lazily, null)

    fun updatePlayoffsActive(enabled: Boolean) {
        applicationScope.launch {
            settings.value?.let {
                dao.saveSettings(it.copy(playoffsActive = enabled))
            }
        }
    }

    fun updateDeviceRobotAssignment(assignment: String) {
        applicationScope.launch {
            settings.value?.let {
                dao.saveSettings(it.copy(deviceRobotAssignment = assignment))
            }
        }
    }

    fun updateCurrentEvent(customEventName: String) {
        applicationScope.launch {
            settings.value?.let {
                dao.saveSettings(it.copy(currentEvent = customEventName))
            }
        }
    }

    suspend fun restoreDefaultSettings() {
        val defaultSettings = AppSettings(
            playoffsActive = false,
            deviceRobotAssignment = "Red 1",
            currentEvent = ""
        )

        dao.saveSettings(defaultSettings)
    }
}