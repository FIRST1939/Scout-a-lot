package com.frcteam1939.scouting2026.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frcteam1939.scouting2026.data.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT name FROM events")
    fun getEventNames(): Flow<List<String>>

    @Query("SELECT * FROM events WHERE name = :eventName")
    fun getEventByName(eventName: String): Flow<Event?>

    @Query("SELECT name FROM events WHERE pit_form_name = :formName OR match_form_name = :formName")
    fun getEventsByFormName(formName: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createEvent(event: Event)

    @Query("DELETE FROM events WHERE name = :eventName")
    fun deleteEvent(eventName: String)

    @Query("UPDATE events SET match_info = :matchInfo where name = :name")
    fun updateEventMatchInfo(name: String, matchInfo: String)

    @Query("UPDATE events SET pit_form_name = :pitFormName where name = :name")
    fun updateEventPitForm(name: String, pitFormName: String)

    @Query("UPDATE events SET match_form_name = :matchFormName where name = :name")
    fun updateEventMatchForm(name: String, matchFormName: String)
}
