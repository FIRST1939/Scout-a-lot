package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class ScoutingViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings
}