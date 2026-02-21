package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.viewmodels.EditFormsViewModel
import kotlinx.coroutines.launch

@Composable
fun EditFormsScreen(navController: NavController, editFormsViewModel: EditFormsViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pit Forms:",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            )
            UserInputs.OutlinedButtonTextBarInput(
                text = "Create Pit Form",
                icon = Icons.Default.Add,
                onClick = {
                    navController.navigate(AppRoutes.EditForms.CREATE_FORM)
                }
            )
            editFormsViewModel.getPitFormNames().collectAsState(initial = emptyList()).value.forEach { formName ->
                UserInputs.OutlinedButtonTextBarInput(
                    text = formName,
                    icon = Icons.Default.Delete,
                    onClick = {
                        coroutineScope.launch {
                            editFormsViewModel.deleteForm(formName, formType = "pit")
                        }
                    }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Match Forms:",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            )
            UserInputs.OutlinedButtonTextBarInput(
                text = "Create Match Form",
                icon = Icons.Default.Add,
                onClick = {
                    navController.navigate(AppRoutes.EditForms.CREATE_FORM)
                }
            )
            editFormsViewModel.getMatchFormNames().collectAsState(initial = emptyList()).value.forEach { formName ->
                UserInputs.OutlinedButtonTextBarInput(
                    text = formName,
                    icon = Icons.Default.Delete,
                    onClick = {
                        coroutineScope.launch {
                            editFormsViewModel.deleteForm(formName, formType = "match")
                        }
                    }
                )
            }
        }

        UserInputs.OutlinedButtonTextBarInput(
            text = "Assign Forms to Events",
            icon = Icons.Default.ArrowForward,
            onClick = {
                navController.navigate(AppRoutes.EditForms.ASSIGN_FORMS)
            }
        )
    }
}

@Composable
fun CreateFormsScreen(navController: NavController, editFormsViewModel: EditFormsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val formType = "pit" // Placeholder until match forms are implemented... TODO: implement match forms

    var nameFormPopup by remember { mutableStateOf(false) }
    var formName by remember { mutableStateOf("") }

    if(nameFormPopup) {
        AlertDialog(
            onDismissRequest = { nameFormPopup = false },
            title = { Text("Please name this form:", color = MaterialTheme.colorScheme.onPrimary) },
            containerColor = MaterialTheme.colorScheme.primary,

            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserInputs.TextboxInput(
                        value = formName,
                        onValueChange = { formName = it },
                        label = "Form Name",
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Keep editing button
                        UserInputs.TextButtonInput(
                            onClick = {
                                nameFormPopup = false
                            },
                            text = "Keep Editing",
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                        // Save button
                        UserInputs.TextButtonInput(
                            onClick = {
                                if (formName.isNotBlank()) {
                                    coroutineScope.launch {
                                        editFormsViewModel.saveForm(formName, formType)
                                    }
                                    nameFormPopup = false
                                    navController.navigate(AppRoutes.EditForms.EDIT_FORMS)
                                }
                            },
                            text = "Save",
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(all = 16.dp)
                        )

                    }
                }
            },

            dismissButton = {
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            UserInputs.TextButtonInput(
                onClick = {
                    navController.navigate(AppRoutes.EditForms.EDIT_FORMS)
                },
                text = "Back",
                modifier = Modifier.padding(all = 16.dp)
            )
            UserInputs.TextButtonInput(
                onClick = {
                    nameFormPopup = true
                },
                text = "Save",
                modifier = Modifier.padding(all = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
            )
        }
        UserInputs.OutlinedButtonInput(
            onClick = { /* TODO: add data field creation */ },
            icon = Icons.Default.Add,
            buttonText = "Add data field"
        )
    }
}

@Composable
fun AssignFormsScreen(navController: NavController, editFormsViewModel: EditFormsViewModel) {
    // Get the event names
    val eventNames by editFormsViewModel
        .getEventNames()
        .collectAsState(initial = emptyList())
    val pitFormNames by editFormsViewModel.getPitFormNames().collectAsState(initial = emptyList())
    val pitFormNamesWithNoneOption = listOf("None Selected").plus(pitFormNames)

    val matchFormNames by editFormsViewModel.getMatchFormNames().collectAsState(initial = emptyList())
    val matchFormNamesWithNoneOption = listOf("None Selected").plus(matchFormNames)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        eventNames.forEach { eventName ->
            var selectedPitForm by remember { mutableStateOf("None Selected") }
            var selectedMatchForm by remember { mutableStateOf("None Selected") }

            LaunchedEffect(eventName) {
                selectedPitForm = editFormsViewModel.getPitFormByEvent(eventName)?.ifBlank { "None Selected" } ?: "None Selected"
                selectedMatchForm = editFormsViewModel.getMatchFormByEvent(eventName)?.ifBlank { "None Selected" } ?: "None Selected"
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = eventName,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                )
                key(selectedPitForm) {
                    UserInputs.DropdownTextBarInput(
                        options = pitFormNamesWithNoneOption,
                        selectedOption = selectedPitForm,
                        onOptionSelected = { selected ->
                            editFormsViewModel.updateEventPitForm(
                                eventName = eventName,
                                pitFormName = if (selected == "None Selected") "" else selected
                            )
                        },
                        text = "Pit Form:"
                    )
                }
                key(selectedMatchForm) {
                    UserInputs.DropdownTextBarInput(
                        options = matchFormNamesWithNoneOption,
                        selectedOption = selectedMatchForm,
                        onOptionSelected = { selected ->
                            editFormsViewModel.updateEventMatchForm(
                                eventName = eventName,
                                matchFormName = if (selected == "None Selected") "" else selected
                            )
                        },
                        text = "Match Form:"
                    )
                }
            }
        }
    }
}