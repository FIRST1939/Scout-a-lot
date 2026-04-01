package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.AppDatabase
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ScoutedDataRepository(private val db: AppDatabase, private val applicationScope: CoroutineScope) {
    private val dao = db.scoutedDataDao()

    fun getPitData(team: String, formName: String, eventName: String): Flow<ScoutedData?> {
        return dao.getPitDataNullable(team, formName, eventName)
    }

    fun insertPitData(scoutedData: ScoutedData, eventName: String) {
        applicationScope.launch {
            if (dao.getPitDataNullable(scoutedData.team, "OKC Pit Form 2026", eventName).first()?.scoutedData != null) {
                dao.updatePitData(scoutedData.scoutedData, scoutedData.team, "OKC Pit Form 2026", eventName)
            } else {
                dao.insertData(scoutedData)
            }
        }
    }

    fun getMatchData(formName: String, eventName: String): Flow<List<ScoutedData>> {
        return dao.getMatchData(formName, eventName)
    }

    fun getMatchByTeamAndNumber(team: String, matchNumber: Int, formName: String, eventName: String): Flow<ScoutedData?> {
        return dao.getMatchByTeamAndNumber(team, matchNumber, formName, eventName)
    }

    fun getAllMatchesForTeam(team: String, formName: String, eventName: String): Flow<List<ScoutedData>> {
        return dao.getAllMatchesForTeam(team, formName, eventName)
    }

    fun insertMatchData(scoutedData: ScoutedData) {
        applicationScope.launch {
            dao.insertData(scoutedData)
        }
    }
}