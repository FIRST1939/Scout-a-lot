package com.frcteam1939.scouting2026

import android.content.Context

actual class TBAAuth (private val context: Context) {
    private val KeyFile = "tba_auth_key.txt"

    // Fetch private key from assets
    actual fun getTBAKey(): String {
        return context.assets.open(KeyFile)
            .bufferedReader()
            .use { it.readText() }
    }
}
