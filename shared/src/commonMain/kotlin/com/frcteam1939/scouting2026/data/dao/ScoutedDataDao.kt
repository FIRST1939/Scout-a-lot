package com.frcteam1939.scouting2026.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoutedDataDao {
    @Query("SELECT * FROM scouted_data WHERE team = :team AND form_name = :formName AND form_type = 'pit'")
    fun getPitData(team: String, formName: String): Flow<ScoutedData>
    // TODO: could get two ScoutedData objects -- test for this later

    @Query("UPDATE scouted_data SET scouted_data = :scoutedData WHERE team = :team AND form_name = :formName AND form_type = 'pit'")
    fun updatePitData(scoutedData: String, team: String, formName: String)

    @Insert()
    fun insertData(scoutedData: ScoutedData)
}
