package com.frcteam1939.scouting2026.storage

expect fun exportTextFile(fileName: String, content: String)

expect fun readTextFile(fileName: String): String?
