package com.frcteam1939.scouting2026.data

import android.content.Context
import androidx.room.Room

lateinit var appContext: Context

fun initAppContextForDB(context: Context) {
    appContext = context.applicationContext
}

actual fun getDatabase(): AppDatabase {
    val dbFile = appContext.getDatabasePath("scouting.db")
    return Room.databaseBuilder(
        context = appContext,
        klass = AppDatabase::class.java,
        name = dbFile.absolutePath
    ).build()
}