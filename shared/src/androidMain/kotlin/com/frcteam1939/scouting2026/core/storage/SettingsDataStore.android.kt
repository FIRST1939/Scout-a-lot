package com.frcteam1939.scouting2026.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.frcteam1939.scouting2026.applicationContext as appContext

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

actual fun getSettingsDataStore(): DataStore<Preferences> {
    return appContext.settingsDataStore
}
