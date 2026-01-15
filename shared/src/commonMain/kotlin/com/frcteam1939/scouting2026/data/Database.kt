package com.frcteam1939.scouting2026.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frcteam1939.scouting2026.data.dao.SettingsDao
import com.frcteam1939.scouting2026.data.entities.AppSettings

@Database(entities = [AppSettings::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}

expect fun getDatabase(): AppDatabase