package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class DataViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings
    val currentEvent = settings.value?.currentEvent ?: ""

    suspend fun getTeamList(): List<String> {
        return eventsRepository.getEventByName(currentEvent).first()?.eventTeamList
            ?.mapNotNull { it.toIntOrNull() }
            ?.sorted()
            ?.map { it.toString() }
            ?: emptyList()
    }
}