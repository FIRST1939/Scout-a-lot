package com.frcteam1939.scouting2026.core.database

import com.frcteam1939.scouting2026.core.database.converter.Converters

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frcteam1939.scouting2026.core.database.dao.EventCodesDao
import com.frcteam1939.scouting2026.core.database.dao.EventsDao
import com.frcteam1939.scouting2026.core.database.dao.FormsDao
import com.frcteam1939.scouting2026.core.database.dao.ScoutedDataDao
import com.frcteam1939.scouting2026.core.database.dao.SettingsDao
import com.frcteam1939.scouting2026.core.database.entity.AppSettings
import com.frcteam1939.scouting2026.core.database.entity.Event
import com.frcteam1939.scouting2026.core.database.entity.EventCodes
import com.frcteam1939.scouting2026.core.database.entity.ScoutedData
import com.frcteam1939.scouting2026.core.database.entity.Form

@Database(entities = [AppSettings::class, Event::class, EventCodes::class, ScoutedData::class, Form::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun eventsDao(): EventsDao
    abstract fun eventCodesDao(): EventCodesDao
    abstract fun scoutedDataDao(): ScoutedDataDao
    abstract fun formsDao(): FormsDao
}

expect fun getDatabase(): AppDatabase
