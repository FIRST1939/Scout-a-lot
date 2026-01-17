package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcteam1939.scouting2026.data.dao.SettingsDao
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlin.Boolean

class SettingsViewModel(private val dao: SettingsDao) : ViewModel() {
    val settingsRepository = SettingsRepository(dao = dao, viewModelScope = viewModelScope)
    val settings: StateFlow<AppSettings?> = settingsRepository.settings

    fun updateExampleSetting(enabled: Boolean) {
        settingsRepository.updateExampleSetting(enabled)
    }

    fun updateDeviceRobotAssignment(assignment: String) {
        settingsRepository.updateDeviceRobotAssignment(assignment)
    }

    fun updateCurrentEvent(eventName: String) {
        TODO("Not yet implemented")
    }
}