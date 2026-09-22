package com.frcteam1939.scouting2026

import com.frcteam1939.scouting2026.feature.forms.EditFormsViewModel
import com.frcteam1939.scouting2026.feature.reports.ChecklistViewModel
import com.frcteam1939.scouting2026.feature.reports.DataViewModel
import com.frcteam1939.scouting2026.feature.scouting.ScoutingViewModel
import com.frcteam1939.scouting2026.feature.settings.SettingsViewModel
import com.frcteam1939.scouting2026.feature.sync.FileSyncViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.frcteam1939.scouting2026.feature.events.EventsRepository
import com.frcteam1939.scouting2026.feature.settings.SettingsRepository
import com.frcteam1939.scouting2026.feature.events.EventCodesRepository
import com.frcteam1939.scouting2026.feature.scouting.ScoutedDataRepository
import com.frcteam1939.scouting2026.feature.forms.FormsRepository

actual class ViewModelFactory actual constructor(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val eventCodesRepository: EventCodesRepository,
    private val scoutedDataRepository: ScoutedDataRepository,
    private val formsRepository: FormsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(settingsRepository, eventsRepository, eventCodesRepository) as T
            }
            modelClass.isAssignableFrom(ScoutingViewModel::class.java) -> {
                ScoutingViewModel(settingsRepository, eventsRepository, scoutedDataRepository) as T
            }
            modelClass.isAssignableFrom(FileSyncViewModel::class.java) -> {
                FileSyncViewModel(settingsRepository, eventsRepository, scoutedDataRepository) as T
            }
            modelClass.isAssignableFrom(ChecklistViewModel::class.java) -> {
                ChecklistViewModel(settingsRepository, eventsRepository, scoutedDataRepository) as T
            }
            modelClass.isAssignableFrom(DataViewModel::class.java) -> {
                DataViewModel(settingsRepository, eventsRepository, scoutedDataRepository) as T
            }
            modelClass.isAssignableFrom(EditFormsViewModel::class.java) -> {
                EditFormsViewModel(eventsRepository, formsRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }
}
