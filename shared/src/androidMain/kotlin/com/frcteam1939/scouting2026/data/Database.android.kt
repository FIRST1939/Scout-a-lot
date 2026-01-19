package com.frcteam1939.scouting2026.data

import android.content.Context
import androidx.room.Room

// This lateinit structure is terrible but it works for now
lateinit var applicationContext: Context

fun initAppContextForDB(context: Context) {
    applicationContext = context.applicationContext
}

actual fun getDatabase(): AppDatabase {
    val dbFile = applicationContext.getDatabasePath("scouting.db")
    return Room.databaseBuilder(
        context = applicationContext,
        klass = AppDatabase::class.java,
        name = dbFile.absolutePath
    ).build()
}