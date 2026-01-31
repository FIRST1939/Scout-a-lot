package com.frcteam1939.scouting2026.data

import android.content.Context
import androidx.room.Room
import com.frcteam1939.scouting2026.applicationContext

actual fun getDatabase(): AppDatabase {
    val dbFile = appContext.getDatabasePath("scouting.db")
    val builder = if (dbFile != null) {
        Room.databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            name = dbFile.absolutePath
        )
    } else {
        // Preview/mock contexts can return null for getDatabasePath.
        Room.inMemoryDatabaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java
        )
    }
    return builder.build()
}
