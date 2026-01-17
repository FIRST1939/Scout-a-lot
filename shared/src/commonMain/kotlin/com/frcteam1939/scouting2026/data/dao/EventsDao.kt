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

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createEvent(event: Event)

    @Query("DELETE FROM events WHERE name = :eventName")
    fun deleteEvent(eventName: String)
}
