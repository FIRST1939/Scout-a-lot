package com.frcteam1939.scouting2026.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frcteam1939.scouting2026.data.entities.EventCodes
import kotlinx.coroutines.flow.Flow

@Dao
interface EventCodesDao {
    @Query("SELECT * FROM event_codes WHERE year = :year")
    fun getEventCodesByYear(year: Int): Flow<EventCodes?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEventCodes(eventCodes: EventCodes)
}
