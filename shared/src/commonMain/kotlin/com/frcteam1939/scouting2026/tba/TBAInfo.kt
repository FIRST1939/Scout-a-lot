package com.frcteam1939.scouting2026.tba

import com.frcteam1939.scouting2026.showAlert
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.headers
import kotlinx.serialization.json.Json


expect class TBAAuth() {
    fun getTBAKey(): String
}

class TBAInfo(private val tbaAuth: TBAAuth) {
    private val client = HttpClient()

    suspend fun pullFromTBA(endpoint: String): String {
        // must suspend, client.get waits for response
        return try {
            val response = client.get("https://www.thebluealliance.com/api/v3${endpoint}") {
                headers {
                    append("X-TBA-Auth-Key", tbaAuth.getTBAKey())
                    append("User-Agent", "Mozilla/5.0")
                }
            }
            return response.bodyAsText()
        } catch (e: Exception) {
            println(e.stackTraceToString())
            showAlert(
                title = "Network Error",
                message = "Failed to pull event keys from TBA. " +
                        "Please check internet connection and try again."
            )
            ""
        }
    }

    suspend fun pullEventKeyList(year: Int): List<String> {
        val jsonString = this.pullFromTBA("/events/${year}/keys")
        if (jsonString.isNotBlank()) {
            val keyList = Json.decodeFromString<List<String>>(jsonString)
            return keyList.map { it.substring(4) } // To remove the year from the key
        } else {
            return emptyList()
        }
    }

    suspend fun pullEventMatches(eventKey: String): String {
        val jsonString = this.pullFromTBA("/event/${eventKey}/matches/simple")
        return jsonString.ifBlank {
            "[]"
        }
    }

    suspend fun pullEventTeams(eventKey: String): List<String> {
        val jsonString = this.pullFromTBA("/event/${eventKey}/teams/keys")
        println("it worked: $jsonString")
        if(jsonString.isNotBlank()) {
            val keyList = Json.decodeFromString<List<String>>(jsonString)
            return keyList.map { it.substring(3) }
        } else {
            return emptyList()
        }
    }
}
