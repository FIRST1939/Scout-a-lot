package com.frcteam1939.scouting2026.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "event_codes", indices = [Index(value = ["year"], unique = true)])
data class EventCodes(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "event_codes") val eventCodes: List<String>,
)
