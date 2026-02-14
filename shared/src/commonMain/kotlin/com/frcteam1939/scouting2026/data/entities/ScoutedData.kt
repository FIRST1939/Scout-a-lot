package com.frcteam1939.scouting2026.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scouted_data")
data class ScoutedData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "team") val team: String,
    @ColumnInfo(name = "match_number") val matchNumber: String,
    @ColumnInfo(name = "form_name") val formName: String,
    @ColumnInfo(name = "form_type") val formType: String,
    @ColumnInfo(name = "scouted_data") val scoutedData: String,
)
