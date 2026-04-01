package com.frcteam1939.scouting2026.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamAvatar(
    @SerialName("teamNumber")
    val teamNumber: Int,
    @SerialName("encodedAvatar")
    val encodedAvatar: String
)

@Serializable
data class FRCAvatars(
    @SerialName("teams")
    val teams: List<TeamAvatar>,
    @SerialName("teamCountTotal")
    val teamCountTotal: Int,
    @SerialName("teamCountPage")
    val teamCountPage: Int,
    @SerialName("pageCurrent")
    val pageCurrent: Int,
    @SerialName("pageTotal")
    val pageTotal: Int
)