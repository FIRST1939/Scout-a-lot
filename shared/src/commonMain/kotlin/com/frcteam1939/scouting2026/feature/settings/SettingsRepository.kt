package com.frcteam1939.scouting2026.feature.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.frcteam1939.scouting2026.feature.settings.model.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val PLAYOFFS_ACTIVE_KEY = booleanPreferencesKey("playoffs_active")
private val DEVICE_ROBOT_ASSIGNMENT_KEY = stringPreferencesKey("device_robot_assignment")
private val CURRENT_EVENT_KEY = stringPreferencesKey("current_event")

private const val DEFAULT_PLAYOFFS_ACTIVE = false
private const val DEFAULT_DEVICE_ROBOT_ASSIGNMENT = "Red 1"
private const val DEFAULT_CURRENT_EVENT = ""

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
    private val applicationScope: CoroutineScope
) {
    val settings: StateFlow<AppSettings?> = dataStore.data
        .map { preferences -> toAppSettings(preferences) }
        .stateIn(applicationScope, SharingStarted.Lazily, null)

    fun updatePlayoffsActive(enabled: Boolean) {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences[PLAYOFFS_ACTIVE_KEY] = enabled
            }
        }
    }

    fun updateDeviceRobotAssignment(assignment: String) {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences[DEVICE_ROBOT_ASSIGNMENT_KEY] = assignment
            }
        }
    }

    fun updateCurrentEvent(customEventName: String) {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences[CURRENT_EVENT_KEY] = customEventName
            }
        }
    }

    private fun toAppSettings(preferences: Preferences): AppSettings {
        return AppSettings(
            playoffsActive = preferences[PLAYOFFS_ACTIVE_KEY] ?: DEFAULT_PLAYOFFS_ACTIVE,
            deviceRobotAssignment = preferences[DEVICE_ROBOT_ASSIGNMENT_KEY]
                ?: DEFAULT_DEVICE_ROBOT_ASSIGNMENT,
            currentEvent = preferences[CURRENT_EVENT_KEY] ?: DEFAULT_CURRENT_EVENT
        )
    }
}
