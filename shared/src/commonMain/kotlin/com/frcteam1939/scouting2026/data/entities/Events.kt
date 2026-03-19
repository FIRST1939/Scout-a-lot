package com.frcteam1939.scouting2026.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "events", indices = [Index(value = ["name"], unique = true)])
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val customEventName: String,
    @ColumnInfo(name = "code") val eventCode: String,
    @ColumnInfo(name = "year") val eventYear: Int,
    @ColumnInfo(name = "match_info") val matchInfo: String,
    @ColumnInfo(name = "team_list") val eventTeamList: List<String>,
    @ColumnInfo(name = "team_name_list") val eventTeamNameList: List<String>,
    @ColumnInfo(name = "pit_form_name") val pitForm: String,
    @ColumnInfo(name = "match_form_name") val matchForm: String
)
