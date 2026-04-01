package com.frcteam1939.scouting2026.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoutedDataDao {
    @Query("SELECT * FROM scouted_data WHERE team = :team AND form_name = :formName AND form_type = 'pit' AND event_name = :eventName LIMIT 1")
    fun getPitData(team: String, formName: String, eventName: String): Flow<ScoutedData>

    @Query("SELECT * FROM scouted_data WHERE team = :team AND form_name = :formName AND form_type = 'pit' AND event_name = :eventName LIMIT 1")
    fun getPitDataNullable(team: String, formName: String, eventName: String): Flow<ScoutedData?>

    @Query("UPDATE scouted_data SET scouted_data = :scoutedData WHERE team = :team AND form_name = :formName AND form_type = 'pit' AND event_name = :eventName")
    fun updatePitData(scoutedData: String, team: String, formName: String, eventName: String)

    @Query("SELECT * FROM scouted_data WHERE form_name = :formName AND form_type = 'match' AND event_name = :eventName")
    fun getMatchData(formName: String, eventName: String): Flow<List<ScoutedData>>

    @Query("SELECT * FROM scouted_data WHERE team = :team AND match_number = :matchNumber AND form_name = :formName AND form_type = 'match' AND event_name = :eventName LIMIT 1")
    fun getMatchByTeamAndNumber(team: String, matchNumber: Int, formName: String, eventName: String): Flow<ScoutedData?>

    @Query("SELECT * FROM scouted_data WHERE team = :team AND form_name = :formName AND form_type = 'match' AND event_name = :eventName")
    fun getAllMatchesForTeam(team: String, formName: String, eventName: String): Flow<List<ScoutedData>>

    @Insert()
    fun insertData(scoutedData: ScoutedData)
}
