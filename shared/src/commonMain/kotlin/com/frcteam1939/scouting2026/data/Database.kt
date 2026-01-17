package com.frcteam1939.scouting2026.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frcteam1939.scouting2026.data.dao.EventsDao
import com.frcteam1939.scouting2026.data.dao.SettingsDao
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.entities.Event

@Database(entities = [AppSettings::class, Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun eventsDao(): EventsDao
}

expect fun getDatabase(): AppDatabase