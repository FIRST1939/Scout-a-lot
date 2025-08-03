package com.frcteam1939.scouting2026

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform