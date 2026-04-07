package com.frcteam1939.scouting2026.viewmodels

import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.EventCodesRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository

actual class ViewModelFactory actual constructor(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val eventCodesRepository: EventCodesRepository,
    private val scoutedDataRepository: ScoutedDataRepository,
    private val formsRepository: FormsRepository
)
