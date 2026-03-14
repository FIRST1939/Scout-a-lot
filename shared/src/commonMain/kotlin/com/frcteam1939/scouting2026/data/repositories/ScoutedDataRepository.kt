package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.AppDatabase
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ScoutedDataRepository(private val db: AppDatabase, private val applicationScope: CoroutineScope) {
    private val dao = db.scoutedDataDao()

    fun getPitData(team: String, formName: String): Flow<ScoutedData?> {
        return dao.getPitDataNullable(team, formName)
    }

    fun insertPitData(scoutedData: ScoutedData, team: String) {
        applicationScope.launch {
            if (dao.getPitDataNullable(scoutedData.team, "OKC Pit Form 2026").first()?.scoutedData != null) {
                dao.updatePitData(scoutedData.scoutedData, scoutedData.team, "OKC Pit Form 2026")
            } else {
                dao.insertData(scoutedData)
            }
        }
    }

    fun getMatchData(formName: String): Flow<List<ScoutedData>> {
        return dao.getMatchData(formName)
    }

    fun getMatchByTeamAndNumber(team: String, matchNumber: Int, formName: String): Flow<ScoutedData?> {
        return dao.getMatchByTeamAndNumber(team, matchNumber, formName)
    }

    fun getAllMatchesForTeam(team: String, formName: String): Flow<List<ScoutedData>> {
        return dao.getAllMatchesForTeam(team, formName)
    }

    fun insertMatchData(scoutedData: ScoutedData) {
        applicationScope.launch {
            dao.insertData(scoutedData)
        }
    }
}