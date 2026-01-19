package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.AppDatabase
import com.frcteam1939.scouting2026.data.entities.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventsRepository(private val db: AppDatabase, private val applicationScope: CoroutineScope) {
    private val dao = db.eventsDao()

    fun getEventNames(): Flow<List<String>> {
        return dao.getEventNames()
    }

    fun getEventByName(eventName: String): Flow<Event?> {
        return dao.getEventByName(eventName)
    }

    fun createEvent(event: Event) {
        applicationScope.launch {
            try {
                dao.createEvent(event)
            } catch (e: Exception) {
                TODO("Add alert when errors: multiple events of same name")
            }
        }
    }

    fun deleteEvent(eventName: String) {
        applicationScope.launch {
            dao.deleteEvent(eventName)
        }
    }
}