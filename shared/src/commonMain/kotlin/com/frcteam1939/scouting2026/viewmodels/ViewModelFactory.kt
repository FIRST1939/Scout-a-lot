package com.frcteam1939.scouting2026.viewmodels

import com.frcteam1939.scouting2026.data.DataDependencyContainer
import com.frcteam1939.scouting2026.data.repositories.EventCodesRepository
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository

expect class ViewModelFactory(
    settingsRepository: SettingsRepository,
    eventsRepository: EventsRepository,
    eventCodesRepository: EventCodesRepository
)

fun getViewModelFactory(): ViewModelFactory {
    return ViewModelFactory(
        DataDependencyContainer.settingsRepository,
        DataDependencyContainer.eventsRepository,
        DataDependencyContainer.eventCodesRepository
    )
}
