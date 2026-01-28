package com.frcteam1939.scouting2026.data

import com.frcteam1939.scouting2026.TBAInfo
import com.frcteam1939.scouting2026.TBAAuth
import com.frcteam1939.scouting2026.data.repositories.EventCodesRepository
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

// So that multiple instances of these objects aren't created in multiple places
object DataDependencyContainer {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: AppDatabase by lazy { getDatabase() }


    val tbaInfo: TBAInfo by lazy { TBAInfo(TBAAuth()) }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(database, applicationScope)
    }

    val eventsRepository: EventsRepository by lazy {
        EventsRepository(database, applicationScope)
    }

    val eventCodesRepository: EventCodesRepository by lazy {
        EventCodesRepository(database, applicationScope)
    }
}
