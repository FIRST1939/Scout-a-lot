package com.frcteam1939.scouting2026.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,  // Always 1
    val playoffsActive: Boolean,
    val deviceRobotAssignment: String,
    val currentEvent: String
)
