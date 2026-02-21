package com.frcteam1939.scouting2026.data

import androidx.room.Database
import androidx.room.ConstructedBy
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.frcteam1939.scouting2026.data.dao.EventCodesDao
import com.frcteam1939.scouting2026.data.dao.EventsDao
import com.frcteam1939.scouting2026.data.dao.FormsDao
import com.frcteam1939.scouting2026.data.dao.ScoutedDataDao
import com.frcteam1939.scouting2026.data.dao.SettingsDao
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.entities.Event
import com.frcteam1939.scouting2026.data.entities.EventCodes
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import com.frcteam1939.scouting2026.data.entities.Form

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
