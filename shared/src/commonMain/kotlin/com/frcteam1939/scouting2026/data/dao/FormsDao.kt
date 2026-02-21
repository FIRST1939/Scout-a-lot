package com.frcteam1939.scouting2026.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frcteam1939.scouting2026.data.entities.Form
import kotlinx.coroutines.flow.Flow

@Dao
interface FormsDao {
    @Query("SELECT * FROM forms WHERE form_name = :formName AND form_type = 'pit'")
    fun getPitForm(formName: String): Flow<Form>

    @Query("SELECT * FROM forms WHERE form_name = :formName AND form_type = 'match'")
    fun getMatchForm(formName: String): Flow<Form>

    @Query("SELECT form_name FROM forms WHERE form_type = 'pit'")
    fun getPitFormNames(): Flow<List<String>>

    @Query("SELECT form_name FROM forms WHERE form_type = 'match'")
    fun getMatchFormNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createForm(form: Form)

    @Query("DELETE FROM forms WHERE form_name = :formName")
    fun deleteForm(formName: String)

    @Query("UPDATE forms SET form_content = :formContent WHERE form_name = :formName AND form_type = 'pit'")
    fun updatePitForm(formContent: String, formName: String)

    @Query("UPDATE forms SET form_content = :formContent WHERE form_name = :formName AND form_type = 'match'")
    fun updateMatchForm(formContent: String, formName: String)
}