package com.frcteam1939.scouting2026

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.headers


expect class TBAAuth {
    fun getTBAKey(): String
}

class CompInfo(private val tbaAuth: TBAAuth) {
    private val client = HttpClient()

    suspend fun pullCompInfoFromTBA(): String {
        val response = client.get("http://www.thebluealliance.com/api/v3/event/2025mokc/matches") {
            headers {
                append("X-TBA-Auth-Key", tbaAuth.getTBAKey())
                append("User-Agent", "Mozilla/5.0")
            }
        }
        return response.bodyAsText()
    }

    suspend fun pullCompInfo(): String {
        // must suspend, pullCompInfoFromTBA is a suspend function
        // TODO: add logic for sourcing comp info elsewhere

        return this.pullCompInfoFromTBA()
    }
}
