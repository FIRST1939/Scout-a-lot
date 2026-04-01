package com.frcteam1939.scouting2026.api

import com.frcteam1939.scouting2026.applicationContext
import okio.IOException
import kotlin.io.encoding.Base64

actual class FRCEventsAuth actual constructor() {
    private val keyFile = "FRCEventsAuthorization.txt"

    // Fetch private key from assets
    actual fun getFRCEventsKey(): String {
        return try {
            val authInfo = applicationContext.assets.open(keyFile)
                .bufferedReader()
                .use { it.readText().split("\n") }

            val authString = "${authInfo[0].trim()}:${authInfo[1].trim()}"
            val encodedAuthString = Base64.encode(authString.encodeToByteArray())
            "Basic $encodedAuthString"
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}