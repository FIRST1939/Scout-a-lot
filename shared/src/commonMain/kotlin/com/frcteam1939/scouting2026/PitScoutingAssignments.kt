package com.frcteam1939.scouting2026

fun assignPitTeams(teamList: List<String>, deviceRobotAssignment: String): List<String> {
    var deviceTag = 0

    // sorts the teamList to make it consistent and smallest to largest
    val sortedTeamList = teamList.mapNotNull { it.toIntOrNull() }.sorted().map { it.toString() }
    val deviceTeamList = mutableListOf<String>()
    when(deviceRobotAssignment) {
        "Red 1" -> deviceTag = 0
        "Red 2" -> deviceTag = 1
        "Red 3" -> deviceTag = 2
        "Blue 1" -> deviceTag = 3
        "Blue 2" -> deviceTag = 4
        "Blue 3" -> deviceTag = 5
    }
    while (deviceTag < sortedTeamList.size) {
        deviceTeamList.add(sortedTeamList[deviceTag])
        deviceTag += 6
    }
    return (deviceTeamList)
}