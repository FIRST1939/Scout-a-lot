package com.frcteam1939.scouting2026.data

import com.frcteam1939.scouting2026.api.TBA
import com.frcteam1939.scouting2026.api.TBAAuth
import com.frcteam1939.scouting2026.data.repositories.EventCodesRepository
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

// So that multiple instances of these objects aren't created in multiple places
object DataDependencyContainer {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: AppDatabase by lazy { getDatabase() }

    val tba: TBA by lazy { TBA(TBAAuth()) }

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
