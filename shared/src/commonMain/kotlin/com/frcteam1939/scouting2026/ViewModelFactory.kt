package com.frcteam1939.scouting2026

import com.frcteam1939.scouting2026.DataDependencyContainer
import com.frcteam1939.scouting2026.feature.events.EventCodesRepository
import com.frcteam1939.scouting2026.feature.events.EventsRepository
import com.frcteam1939.scouting2026.feature.forms.FormsRepository
import com.frcteam1939.scouting2026.feature.scouting.ScoutedDataRepository
import com.frcteam1939.scouting2026.feature.settings.SettingsRepository

expect class ViewModelFactory(
    settingsRepository: SettingsRepository,
    eventsRepository: EventsRepository,
    eventCodesRepository: EventCodesRepository,
    scoutedDataRepository: ScoutedDataRepository,
    formsRepository: FormsRepository
)

fun getViewModelFactory(): ViewModelFactory {
    return ViewModelFactory(
        DataDependencyContainer.settingsRepository,
        DataDependencyContainer.eventsRepository,
        DataDependencyContainer.eventCodesRepository,
        DataDependencyContainer.scoutedDataRepository,
        DataDependencyContainer.formsRepository
    )
}
