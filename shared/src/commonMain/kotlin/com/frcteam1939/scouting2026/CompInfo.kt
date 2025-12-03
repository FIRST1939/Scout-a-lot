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

    suspend fun pullFromTBA(eventKey: String, type: String): String {
        val response = client.get("https://www.thebluealliance.com/api/v3/event/${eventKey}/${type}") {
            headers {
                append("X-TBA-Auth-Key", tbaAuth.getTBAKey())
                append("User-Agent", "Mozilla/5.0")
            }
        }
        return response.bodyAsText()
    }

    suspend fun pullCompMatches(eventKey: String): String {
        // must suspend, pullCompMatchesFromTBA is a suspend function
        // TODO: add logic for sourcing comp info elsewhere, stored on-device

        return this.pullFromTBA(eventKey, type="matches")
    }

    suspend fun pullCompTeams(eventKey: String): String {
        // must suspend, pullCompTeamsFromTBA is a suspend function
        // TODO: add logic for sourcing comp info elsewhere, stored on-device

        return this.pullFromTBA(eventKey, type="teams")
    }
}
