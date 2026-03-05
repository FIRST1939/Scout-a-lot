package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository

class DataViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository
) : ViewModel() {

}