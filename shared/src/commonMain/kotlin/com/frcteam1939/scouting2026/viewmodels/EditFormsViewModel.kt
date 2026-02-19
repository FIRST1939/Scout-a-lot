package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonArray

class EditFormsViewModel(
    private val eventsRepository: EventsRepository,
    private val formsRepository: FormsRepository
) : ViewModel() {
    fun getEventNames(): Flow<List<String>> {
        return eventsRepository.getEventNames()
    }

    suspend fun getPitFormByEvent(eventName: String): String? {
        return eventsRepository.getEventByName(eventName).first()?.pitForm
    }

    suspend fun getMatchFormByEvent(eventName: String): String? {
        return eventsRepository.getEventByName(eventName).first()?.matchForm
    }

    fun getPitFormNames(): Flow<List<String>> {
        return formsRepository.getPitFormNames()
    }

    fun getMatchFormNames(): Flow<List<String>> {
        return formsRepository.getMatchFormNames()
    }

    fun updateEventPitForm(eventName: String, pitFormName: String) {
        eventsRepository.updateEventPitForm(eventName, pitFormName)
    }

    fun updateEventMatchForm(eventName: String, matchFormName: String) {
        eventsRepository.updateEventMatchForm(eventName, matchFormName)
    }

    fun updateForm(formName: String, formType: String, formContent: JsonArray) {
        formsRepository.updateFormContent(formName, formType, formContent)
    }

    fun createForm(formName: String, formType: String, formContent: JsonArray) {
        formsRepository.createForm(formName, formType, formContent)
    }

    suspend fun saveForm(formName: String, formType: String) {
        val formContent = buildJsonArray {}

        val existingFormNames = when (formType) {
            "pit" -> getPitFormNames().first()
            "match" -> getMatchFormNames().first()
            else -> emptyList()
        }

        if (formName in existingFormNames) {
            updateForm(formName, formType, formContent)
        } else {
            createForm(formName, formType, formContent)
        }

    }
}