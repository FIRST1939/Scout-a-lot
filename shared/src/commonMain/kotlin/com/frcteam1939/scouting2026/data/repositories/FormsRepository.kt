package com.frcteam1939.scouting2026.data.repositories

import com.frcteam1939.scouting2026.data.AppDatabase
import com.frcteam1939.scouting2026.data.entities.Form
import com.frcteam1939.scouting2026.showAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

class FormsRepository(private val db: AppDatabase, private val applicationScope: CoroutineScope) {
    private val dao = db.formsDao()

    fun getPitForm(formName: String): Flow<Form?> {
        return dao.getPitForm(formName)
    }

    fun getMatchForm(formName: String): Flow<Form?> {
        return dao.getMatchForm(formName)
    }

    fun getPitFormNames(): Flow<List<String>> {
        return dao.getPitFormNames()
    }

    fun getMatchFormNames(): Flow<List<String>> {
        return dao.getMatchFormNames()
    }

    fun createForm(formName: String, formType: String, formContent: JsonArray) {
        val formContentString = Json.encodeToString(formContent)

        applicationScope.launch {
            try {
                dao.createForm(Form(0, formName, formType, formContentString))
            } catch (e: Exception) {
                showAlert(title = "Cannot create form.", message = "A form with this name already exists.")
            }
        }
    }

    fun deleteForm(formName: String) {
        applicationScope.launch {
            dao.deleteForm(formName)
        }
    }

    fun updatePitFormContent(formContent: JsonArray, formName: String) {
        val formContentString = Json.encodeToString(formContent)

        applicationScope.launch {
            dao.updatePitForm(formContentString, formName)
        }
    }

    fun updateMatchFormContent(formContent: JsonArray, formName: String) {
        val formContentString = Json.encodeToString(formContent)

        applicationScope.launch {
            dao.updateMatchForm(formContentString, formName)
        }
    }
}
