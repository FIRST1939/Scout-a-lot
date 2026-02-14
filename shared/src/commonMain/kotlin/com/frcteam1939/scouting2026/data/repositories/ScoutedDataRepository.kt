package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.AppDatabase
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScoutedDataRepository(private val db: AppDatabase, private val applicationScope: CoroutineScope) {
    private val dao = db.scoutedDataDao()

    fun getPitData(team: String, formName: String): Flow<ScoutedData> {
        return dao.getPitData(team, formName)
    }

    fun insertPitData(scoutedData: ScoutedData) {
        applicationScope.launch {
            dao.insertData(scoutedData)
        }
    }
}