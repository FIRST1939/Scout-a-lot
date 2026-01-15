package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

actual fun <T : ViewModel> viewModelFactory(creator: () -> T): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return creator() as T
        }
    }
}
