package com.frcteam1939.scouting2026.api

import com.frcteam1939.scouting2026.applicationContext
import okio.IOException

actual class TBAAuth actual constructor() {
    private val keyFile = "X-TBA-Auth-Key.txt"

    // Fetch private key from assets
    actual fun getTBAKey(): String {
        return try {
            applicationContext.assets.open(keyFile)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}
