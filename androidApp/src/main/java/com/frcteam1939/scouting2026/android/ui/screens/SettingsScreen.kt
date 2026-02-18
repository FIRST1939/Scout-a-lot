package com.frcteam1939.scouting2026.android.ui.screens

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    // This collects the StateFlow from the settings view model
    val settingsState by settingsViewModel.settings.collectAsState()
    val currentSettings = settingsState ?: return

    // Get the event names
    val eventNames by settingsViewModel.getEventNames().collectAsState(initial = emptyList())
    val eventNamesWithNoneOption = listOf("None Selected").plus(eventNames)
    val selectedEvent = currentSettings.currentEvent.ifBlank { "None Selected" }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp)
        )

        // Select match scouting type
        UserInputs.SwitchTextBarInput(
            checked = currentSettings.playoffsActive,
            onCheckedChange = { settingsViewModel.updatePlayoffsActive(it) },
            text = "Scouting playoffs"
        )

        // Select robot assignment
        UserInputs.DropdownTextBarInput(
            options = listOf("Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3"),
            selectedOption = currentSettings.deviceRobotAssignment,
            onOptionSelected = { settingsViewModel.updateDeviceRobotAssignment(it) },
            text = "Robot and Alliance Assignment"
        )

        // Select current event
        UserInputs.DropdownTextBarInput(
            options = eventNamesWithNoneOption,
            selectedOption = selectedEvent,
            onOptionSelected = { selected ->
                settingsViewModel.updateCurrentEvent(
                    if (selected == "None Selected") "" else selected
                )
            },
            text = "Current Event"
        )

        Text(
            text = "Actions:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp)
        )

        // Add event
        UserInputs.TextButtonInput(
            onClick = { navController.navigate(AppRoutes.Settings.EDIT_EVENTS) },
            text = "Edit Events",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(all = 4.dp),
        )

        // Navigate to operator settings
        UserInputs.TextButtonInput(
            onClick = { navController.navigate(AppRoutes.OPERATOR) },
            text = "Operator Settings",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(all = 4.dp),
        )
    }
}

@Composable
fun EditEventsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val eventNames by settingsViewModel.getEventNames().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInputs.OutlinedButtonTextBarInput(
            text = "Add Event",
            buttonText = "",
            icon = Icons.Default.Add,
            onClick = { navController.navigate(AppRoutes.Settings.ADD_EVENT) }
        )
        UserInputs.OutlinedButtonTextBarInput(
            text = "Refresh Match Info",
            buttonText = "",
            icon = Icons.Default.Refresh,
            onClick = {
                // TODO: I don't think this works... it kinda breaks everything
                coroutineScope.launch {
                    eventNames.forEach {
                        val event = settingsViewModel.getEventByName(it) ?: return@forEach

                        settingsViewModel.updateEventMatchInfo(
                            name = event.customEventName,
                            code = event.eventCode,
                            year = event.eventYear
                        )
                    }
                }
            }
        )
        Text(
            text = "Your Events:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp)
        )
        eventNames.forEach {
            var qualsLoaded by remember { mutableStateOf(false) }
            var playoffsLoaded by remember { mutableStateOf(false) }

            LaunchedEffect(it) {
                qualsLoaded = settingsViewModel.checkQualsMatchLoadStatus(it)
                playoffsLoaded = settingsViewModel.checkPlayoffMatchLoadStatus(it)
            }

            UserInputs.OutlinedButtonTextBarInput(
                text = it,
                buttonText = "",
                icon = Icons.Default.Delete,
                onClick = {
                    settingsViewModel.deleteEvent(it)
                    if (it == settingsViewModel.settings.value?.currentEvent) {
                        settingsViewModel.updateCurrentEvent("")
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (qualsLoaded) {
                    Text(text = "Quals Matches: Loaded!", color = MaterialTheme.colorScheme.primary)
                } else {
                    Text(
                        text = "Quals Matches: Not Loaded.",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                if (playoffsLoaded) {
                    Text(
                        text = "Playoff Matches: Loaded!",
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "Playoff Matches: Not Loaded.",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun AddEventScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var eventName by remember { mutableStateOf("") }
    var eventCode by remember { mutableStateOf("") }
    var eventYear by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Event:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp)
        )

        // Prompt event name
        UserInputs.TextboxInput(
            value = eventName,
            onValueChange = { eventName = it },
            label = "Event Name",
            singleLine = true
        )

        // Prompt event code
        UserInputs.TextboxInput(
            value = eventCode,
            onValueChange = { eventCode = it },
            label = "Event Code",
            singleLine = true
        )

        // Prompt event year
        UserInputs.TextboxInput(
            value = eventYear,
            onValueChange = { eventYear = it },
            label = "Event Year",
            singleLine = true,
            forceNumberInput = true,
            characterLimit = 4
        )

        UserInputs.TextButtonInput(
            onClick = {
                coroutineScope.launch {
                    val isValid =
                        !eventName.isBlank() &&
                                !eventCode.isBlank() &&
                                !eventYear.isBlank() &&
                                settingsViewModel.checkEventValidity(
                                    eventCode,
                                    eventYear.toInt()
                                )
                                ?: false
                    if (isValid) {
                        settingsViewModel.createEvent(eventName, eventCode, eventYear.toInt())
                        navController.navigate(AppRoutes.Settings.EDIT_EVENTS)
                        makeText(context, "Event creation successful!", Toast.LENGTH_SHORT)
                            .show()
                    } else makeText(context, "Invalid event info", Toast.LENGTH_SHORT).show()
                }
            },
            text = "Add Event"
        )
    }
}