package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

expect fun <T : ViewModel> viewModelFactory(creator: () -> T): ViewModelProvider.Factory