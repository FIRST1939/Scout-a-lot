package com.frcteam1939.scouting2026.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TBATeam(
    @SerialName("key")
    val key: String,
    @SerialName("team_number")
    val teamNumber: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("name")
    val name: String,
    @SerialName("city")
    val city: String,
    @SerialName("state_prov")
    val stateProv: String,
    @SerialName("country")
    val country: String,
)