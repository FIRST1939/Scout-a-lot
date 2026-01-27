package com.frcteam1939.scouting2026.data

import android.content.Context
import androidx.room.Room
import com.frcteam1939.scouting2026.applicationContext

actual fun getDatabase(): AppDatabase {
    val dbFile = applicationContext.getDatabasePath("scouting.db")
    return Room.databaseBuilder(
        context = applicationContext,
        klass = AppDatabase::class.java,
        name = dbFile.absolutePath
    ).build()
}