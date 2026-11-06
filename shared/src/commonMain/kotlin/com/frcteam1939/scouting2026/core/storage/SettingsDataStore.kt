package com.frcteam1939.scouting2026.core.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun getSettingsDataStore(): DataStore<Preferences>
