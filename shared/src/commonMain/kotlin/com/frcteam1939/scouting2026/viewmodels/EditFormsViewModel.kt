package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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
}