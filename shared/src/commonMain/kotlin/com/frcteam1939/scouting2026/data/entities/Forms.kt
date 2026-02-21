package com.frcteam1939.scouting2026.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "forms", indices = [Index(value = ["form_name"], unique = true)])
data class Form(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "form_name") val formName: String,
    @ColumnInfo(name = "form_type") val formType: String,
    @ColumnInfo(name = "form_content") val formContent: String,
)
