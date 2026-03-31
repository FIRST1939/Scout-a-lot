package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.viewmodels.ChecklistViewModel

@Composable
fun ChecklistScreen(navController: NavController, checklistViewModel: ChecklistViewModel) {
    Column() {
        Row() {
            Text("Showing collected data for")
        }
    }
}