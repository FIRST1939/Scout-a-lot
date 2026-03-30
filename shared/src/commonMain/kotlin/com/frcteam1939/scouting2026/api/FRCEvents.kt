package com.frcteam1939.scouting2026.api

import com.frcteam1939.scouting2026.api.models.FRCAvatars
import com.frcteam1939.scouting2026.showAlert
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.time.ExperimentalTime

// You can access this service at: https://frc-events.firstinspires.org/services/API

expect class FRCEventsAuth() {
    fun getFRCEventsKey(): String
}

class FRCEvents(private val frcEventsAuth: FRCEventsAuth) {
    private val client = HttpClient()

    @OptIn(ExperimentalTime::class)
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    suspend fun pullFromFRCEvents(endpoint: String): String {
        // must suspend, client.get waits for response
        return try {
            val response = client.get("https://frc-api.firstinspires.org/v3.0/${endpoint}") {
                headers {
                    append("Authorization", frcEventsAuth.getFRCEventsKey())
                }
            }
            response.bodyAsText()
        } catch (e: Exception) {
            println(e.stackTraceToString())
            showAlert(
                title = "Network Error",
                message = "Failed to pull data from FRC Events. " +
                        "Please check internet connection and try again."
            )
            ""
        }
    }

    // returns a list of avatars as pairs where A is team number string, and B is encoded avatar byte array
    suspend fun pullTeamAvatarsByEvent(eventKey: String): List<Pair<String, ByteArray>> {
        val jsonString = this.pullFromFRCEvents("$currentYear/avatars?eventCode=${eventKey}")
        val avatars = Json.decodeFromString<FRCAvatars>(jsonString)

        return avatars.teams.map { avatar ->
            avatar.teamNumber.toString() to Base64.decode(avatar.encodedAvatar)
        }
    }
}
