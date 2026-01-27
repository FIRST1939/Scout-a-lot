package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.frcteam1939.scouting2026.TBAInfo
import com.frcteam1939.scouting2026.data.entities.EventCodes
import kotlinx.coroutines.flow.onStart


class EventCodesRepository(
    private val db: AppDatabase,
    private val applicationScope: CoroutineScope,
    private val tbaInfo: TBAInfo
) {
    private val dao = db.eventCodesDao()

    suspend fun getEventCodes(year: Int): Flow<EventCodes?> {
        if (dao.getEventCodesByYear(year).first() == null) {
            val eventKeys = tbaInfo.pullEventKeyList(year)
            println("Event keys @ repo layer: $eventKeys")
            if (eventKeys.isNotEmpty()) {
                dao.updateEventCodes(EventCodes(id = 0, year = year, eventCodes = eventKeys))
            }
            else {
                println("Failed to pull event codes from TBA")
            }
        }

        return dao.getEventCodesByYear(year)
    }
}