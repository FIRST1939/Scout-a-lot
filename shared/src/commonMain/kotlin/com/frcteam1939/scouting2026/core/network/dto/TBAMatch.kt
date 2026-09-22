package com.frcteam1939.scouting2026.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TBAMatch(
    @SerialName("key")
    val key: String,
    @SerialName("comp_level")
    val compLevel: String,
    @SerialName("set_number")
    val setNumber: Int,
    @SerialName("match_number")
    val matchNumber: Int,
    @SerialName("alliances")
    val alliances: Alliances,
    @SerialName("winning_alliance")
    val winningAlliance: String,
    @SerialName("event_key")
    val eventKey: String,
    @SerialName("time")
    val time: Int,
    @SerialName("predicted_time")
    val predictedTime: Int?,
    @SerialName("actual_time")
    val actualTime: Int?
)

@Serializable
data class Alliances(
    @SerialName("red")
    val red: Alliance,
    @SerialName("blue")
    val blue: Alliance
)

@Serializable
data class Alliance(
    @SerialName("score")
    val score: Int,
    @SerialName("team_keys")
    val teamKeys: List<String>,
    @SerialName("surrogate_team_keys")
    val surrogateTeamKeys: List<String>,
    @SerialName("dq_team_keys")
    val dqTeamKeys: List<String>
)
