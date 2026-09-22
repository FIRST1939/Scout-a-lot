package com.frcteam1939.scouting2026

import com.frcteam1939.scouting2026.feature.events.EventsRepository
import com.frcteam1939.scouting2026.feature.events.EventCodesRepository
import com.frcteam1939.scouting2026.feature.forms.FormsRepository
import com.frcteam1939.scouting2026.feature.scouting.ScoutedDataRepository
import com.frcteam1939.scouting2026.feature.settings.SettingsRepository

actual class ViewModelFactory actual constructor(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val eventCodesRepository: EventCodesRepository,
    private val scoutedDataRepository: ScoutedDataRepository,
    private val formsRepository: FormsRepository
)
