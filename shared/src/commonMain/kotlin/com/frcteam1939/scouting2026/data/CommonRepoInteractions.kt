package com.frcteam1939.scouting2026.data

import com.frcteam1939.scouting2026.data.DataDependencyContainer.eventsRepository
import com.frcteam1939.scouting2026.data.DataDependencyContainer.settingsRepository
import kotlinx.coroutines.flow.first

suspend fun getTeamNameByNumber(teamNumber: String): String {
    val eventName = settingsRepository.settings.value?.currentEvent ?: ""
    val event = eventsRepository.getEventByName(eventName).first()
    val teamList = event?.eventTeamList ?: emptyList()
    val teamNameList = event?.eventTeamNameList ?: emptyList()
    val teamIndex = teamList.indexOf(teamNumber)

    if (teamIndex != -1 && teamIndex < teamNameList.size) {
        return teamNameList[teamIndex]
    } else {
        return "ERROR: Team name not found"
    }
}