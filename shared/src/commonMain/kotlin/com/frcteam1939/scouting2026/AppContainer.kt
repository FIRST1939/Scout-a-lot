package com.frcteam1939.scouting2026

import com.frcteam1939.scouting2026.core.database.AppDatabase
import com.frcteam1939.scouting2026.core.database.getDatabase

import com.frcteam1939.scouting2026.core.network.FRCEvents
import com.frcteam1939.scouting2026.core.network.FRCEventsAuth
import com.frcteam1939.scouting2026.core.network.TBA
import com.frcteam1939.scouting2026.core.network.TBAAuth
import com.frcteam1939.scouting2026.feature.events.EventCodesRepository
import com.frcteam1939.scouting2026.feature.events.EventsRepository
import com.frcteam1939.scouting2026.feature.scouting.ScoutedDataRepository
import com.frcteam1939.scouting2026.feature.forms.FormsRepository
import com.frcteam1939.scouting2026.feature.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

// So that multiple instances of these objects aren't created in multiple places
object DataDependencyContainer {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: AppDatabase by lazy { getDatabase() }

    val tba: TBA by lazy { TBA(TBAAuth()) }
    val frcEvents: FRCEvents by lazy { FRCEvents(FRCEventsAuth()) }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(database, applicationScope)
    }

    val eventsRepository: EventsRepository by lazy {
        EventsRepository(database, applicationScope)
    }

    val eventCodesRepository: EventCodesRepository by lazy {
        EventCodesRepository(database, applicationScope)
    }

    val scoutedDataRepository: ScoutedDataRepository by lazy {
        ScoutedDataRepository(database, applicationScope)
    }

    val formsRepository: FormsRepository by lazy {
        FormsRepository(database, applicationScope)
    }
}
