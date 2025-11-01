package com.frcteam1939.scouting2026

import android.content.Context
import okio.IOException

actual class TBAAuth (private val context: Context) {
    private val keyFile = "X-TBA-Auth-Key.txt`"

    // Fetch private key from assets
    actual fun getTBAKey(): String {
        return try {
            context.assets.open(keyFile)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}
