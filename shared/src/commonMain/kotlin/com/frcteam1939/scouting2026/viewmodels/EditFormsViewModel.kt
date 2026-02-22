package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import com.frcteam1939.scouting2026.showAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonArray

class EditFormsViewModel(
    private val eventsRepository: EventsRepository,
    private val formsRepository: FormsRepository
) : ViewModel() {

    private val _editingFormName = MutableStateFlow("")
    val editingFormName: StateFlow<String> = _editingFormName.asStateFlow()

    fun changeEditingFormName(newEditingFormName: String) {
        _editingFormName.value = newEditingFormName
    }

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

    suspend fun deleteForm(formName: String, formType: String) {
        println("events by form name ($formName): ${eventsRepository.getEventsByFormName(formName).first()}")
        eventsRepository.getEventsByFormName(formName).first().forEach { eventName ->
            when (formType) {
                "pit" -> eventsRepository.updateEventPitForm(eventName, pitFormName = "")
                "match" -> eventsRepository.updateEventMatchForm(eventName,  matchFormName = "")
            }
        }

        formsRepository.deleteForm(formName)
    }

    suspend fun saveForm(formName: String, formType: String) {
        val formContent = buildJsonArray {}

        val existingFormNames = when (formType) {
            "pit" -> getPitFormNames().first()
            "match" -> getMatchFormNames().first()
            else -> emptyList()
        }

        if (formName in existingFormNames) {
            // making sure we're not creating a form with an existing name
            if (editingFormName.value != "") {
                updateForm(formName, formType, formContent)
            } else {
                // TODO: fix this nonsense!! form content should not be deleted
                showAlert(
                    title = "Form Name Already Exists",
                    message = "Could not save form... and what you were just editing has been deleted." +
                            " Sorry!!! Please bug someone to fix this!!!"
                )
            }
        } else {
            createForm(formName, formType, formContent)
        }
    }
}