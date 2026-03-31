package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.WarningBox
import com.frcteam1939.scouting2026.viewmodels.ChecklistViewModel
import kotlinx.coroutines.launch

@Composable
fun ChecklistScreen(navController: NavController, checklistViewModel: ChecklistViewModel) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        checklistViewModel.loadChecklist()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (checklistViewModel.getCurrentEvent() == "") {
            WarningBox("Could not find event. Have you created and selected an event?")
            return@Column
        } else {
            Row(
                modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Showing collected data for event: '${checklistViewModel.getCurrentEvent()}'"
                )
                UserInputs.OutlinedButtonInput(
                    onClick = {
                        coroutineScope.launch {
                            checklistViewModel.loadChecklist()
                        }
                    },
                    icon = Icons.Default.Refresh,
                    buttonText = "Refresh"
                )
            }
        }
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text("Pit data:", style = MaterialTheme.typography.headlineMedium)
        checklistViewModel.pitScoutingAssignments.collectAsState().value.forEach {
            val deviceRobotAssignment = it.first
            val pitScoutingAssignments = it.second

            Divider()
            Row(
                modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = deviceRobotAssignment,
                    color = when (deviceRobotAssignment.take(3)) {
                        "Red" -> Color.Red
                        "Blu" -> Color.Blue
                        else -> MaterialTheme.colorScheme.error
                    }
                )
                pitScoutingAssignments.forEach { team ->
                    if (checklistViewModel.pitScoutedTeams.collectAsState().value.contains(team)) {
                        Text(team, color = Color(0xFF078502))
                    } else {
                        Text(team, color = Color(0xFFD50000))
                    }
                }
            }
        }
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text("Match data:", style = MaterialTheme.typography.headlineMedium)
    }
}