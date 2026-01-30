package com.frcteam1939.scouting2026.viewmodels

import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlin.reflect.KClass

actual class ViewModelFactory actual constructor(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository
)