package com.frcteam1939.scouting2026.api

import com.frcteam1939.scouting2026.showAlert
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText

// You can access this service at: https://frc-events.firstinspires.org/services/API

expect class FRCEventsAuth() {
    fun getFRCEventsKey(): String
}

class FRCEvents(private val frcEventsAuth: FRCEventsAuth) {
    private val client = HttpClient()

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
}