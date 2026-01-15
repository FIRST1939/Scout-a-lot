package com.frcteam1939.scouting2026.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,  // Always 1
    val exampleSetting: Boolean,
    val deviceRobotAssignment: String,
    val currentEvent: String
)
