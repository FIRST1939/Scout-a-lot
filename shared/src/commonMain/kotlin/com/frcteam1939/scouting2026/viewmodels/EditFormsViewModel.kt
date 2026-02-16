package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.FormsRepository
import kotlinx.coroutines.flow.Flow

class EditFormsViewModel(
    private val eventsRepository: EventsRepository,
    private val formsRepository: FormsRepository
) : ViewModel() {
    fun getEventNames(): Flow<List<String>> {
        return eventsRepository.getEventNames()
    }
}