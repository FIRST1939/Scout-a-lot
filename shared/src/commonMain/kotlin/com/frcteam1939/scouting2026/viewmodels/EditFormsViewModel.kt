package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import com.frcteam1939.scouting2026.data.structuring.FormField
import com.frcteam1939.scouting2026.data.structuring.FormFieldManager
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
    private val _selectedFieldTypeToAdd = MutableStateFlow(FormFieldManager.FieldTypes.TEXTBOX)
    private val _currentPage = MutableStateFlow(1)
    // This is split into 3 pages to accommodate match forms being 3 pages
    // Pit forms only use formContentPage1
    private val _formContentPage1 = MutableStateFlow<List<FormField>>(emptyList())
    private val _formContentPage2 = MutableStateFlow<List<FormField>>(emptyList())
    private val _formContentPage3 = MutableStateFlow<List<FormField>>(emptyList())

    val editingFormName: StateFlow<String> = _editingFormName.asStateFlow()
    val selectedFieldTypeToAdd: StateFlow<String> = _selectedFieldTypeToAdd.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val formContentPage1: StateFlow<List<FormField>> = _formContentPage1.asStateFlow()
    val formContentPage2: StateFlow<List<FormField>> = _formContentPage2.asStateFlow()
    val formContentPage3: StateFlow<List<FormField>> = _formContentPage3.asStateFlow()


    fun changeEditingFormName(newEditingFormName: String) {
        _editingFormName.value = newEditingFormName
    }

    fun changeSelectedFieldTypeToAdd(newSelectedFieldTypeToAdd: String) {
        _selectedFieldTypeToAdd.value = newSelectedFieldTypeToAdd
    }

    // Must fire whenever the UI navigates to any of the pages
    fun changeCurrentPage(newCurrentPage: Int) {
        _currentPage.value = newCurrentPage
    }

    // 1 is Auto, 2 is Teleop, 3 is Endgame. For pit scouting, only use page 1
    fun changeFormContent(newFormContent: List<FormField>, page: Int) {
        when (page) {
            1 -> _formContentPage1.value = newFormContent
            2 -> _formContentPage2.value = newFormContent
            3 -> _formContentPage3.value = newFormContent
        }
    }

    fun resetFormContent() {
        changeFormContent(newFormContent = emptyList(), page = 1)
        changeFormContent(newFormContent = emptyList(), page = 2)
        changeFormContent(newFormContent = emptyList(), page = 3)
    }

    fun getFieldTypes(): List<String> {
        return FormFieldManager.FieldTypes.FIELD_LIST
    }

    fun addField() {
        val newID: Int = when (currentPage.value) {
            1 -> formContentPage1.value.size
            2 -> formContentPage1.value.size
            3 -> formContentPage1.value.size
            else -> throw IllegalArgumentException("Invalid page index ${currentPage.value}: must be 1, 2, or 3.")
        }

        val field = when (selectedFieldTypeToAdd.value) {
            FormFieldManager.FieldTypes.TEXTBOX -> FormFieldManager.getTextBoxJson(
                id = newID,
                label = "Unnamed Textbox"
            )
            else -> throw IllegalArgumentException("Invalid field type: ${selectedFieldTypeToAdd.value}")
        }

        changeFormContent(newFormContent = formContentPage1.value.plus(field), page = currentPage.value)
    }

    fun updateFieldParameter(id: Int, parameter: String, value: String) {

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