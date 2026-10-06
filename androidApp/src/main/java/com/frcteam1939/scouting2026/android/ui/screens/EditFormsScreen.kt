package com.frcteam1939.scouting2026.android.ui.screens

import android.widget.Toast
import android.widget.Toast.makeText
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.templates.RadiobuttonFieldCTemplate
import com.frcteam1939.scouting2026.android.ui.templates.SwitchFieldCTemplate
import com.frcteam1939.scouting2026.android.ui.templates.TextboxFieldCTemplate
import com.frcteam1939.scouting2026.feature.scouting.FormFieldManager
import com.frcteam1939.scouting2026.feature.forms.EditFormsViewModel
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
                    editFormsViewModel.changeEditingFormName("")
                    editFormsViewModel.changeCurrentPage(1)
                    editFormsViewModel.resetFormContent()
                    navController.navigate(AppRoutes.EditForms.EDIT_PIT_FORM)
                }
            )
            editFormsViewModel.getPitFormNames().collectAsState(initial = emptyList()).value.forEach { formName ->
                UserInputs.DoubleOutlinedButtonTextBarInput(
                    text = formName,
                    iconOne = Icons.Default.Delete,
                    onButtonOneClick = {
                        coroutineScope.launch {
                            editFormsViewModel.deleteForm(formName, formType = "pit")
                        }
                    },
                    iconTwo = Icons.Default.Edit,
                    onButtonTwoClick = {
                        editFormsViewModel.changeEditingFormName(formName)
                        editFormsViewModel.changeCurrentPage(1)
                        editFormsViewModel.resetFormContent()
                        navController.navigate(AppRoutes.EditForms.EDIT_PIT_FORM)
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
                    navController.navigate(AppRoutes.EditForms.EDIT_MATCH_FORM)
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
fun EditPitFormsScreen(navController: NavController, editFormsViewModel: EditFormsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var nameFormPopup by remember { mutableStateOf(false) }
    val editingFormName = editFormsViewModel.editingFormName.collectAsState().value
    var newFormName by remember { mutableStateOf(editingFormName) }
    val pitFormContent by editFormsViewModel.formContentPage1.collectAsState()

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
                        value = newFormName,
                        onValueChange = { newFormName = it },
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
                                if (newFormName.isNotBlank()) {
                                    coroutineScope.launch {
                                        editFormsViewModel.saveForm(newFormName, formType = "pit")
                                    }
                                    nameFormPopup = false
                                    navController.navigate(AppRoutes.EditForms.EDIT_FORMS)
                                } else {
                                    makeText(context, "Please name this form!", Toast.LENGTH_SHORT).show()
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
            Text(
                text = newFormName.ifBlank { "Unnamed Pit Form" },
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(all = 16.dp)
            )
            // Saves form if it exists, else opens the name form prompt
            UserInputs.TextButtonInput(
                onClick = {
                    if (editingFormName.isBlank()) {
                        nameFormPopup = true
                    } else {
                        coroutineScope.launch {
                            editFormsViewModel.saveForm(editingFormName, formType = "pit")
                        }
                        navController.navigate(AppRoutes.EditForms.EDIT_FORMS)
                    }
                },
                text = "Save",
                modifier = Modifier.padding(all = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
            )
        }
        pitFormContent.forEach { field ->
            Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
            when (field.type) {
                FormFieldManager.FieldTypes.TEXTBOX -> TextboxFieldCTemplate(editFormsViewModel, field.id)
                FormFieldManager.FieldTypes.RADIOBUTTON -> RadiobuttonFieldCTemplate(editFormsViewModel, field.id)
                FormFieldManager.FieldTypes.SWITCH -> SwitchFieldCTemplate(editFormsViewModel, field.id)
                FormFieldManager.FieldTypes.CHECKBOX -> Text("This is a checkbox at one pint 😋")
                FormFieldManager.FieldTypes.COUNTER -> Text("This is a counter at one pint 😨")
                FormFieldManager.FieldTypes.DROPDOWN -> Text("This is a dropdown at one pint 😎")
                FormFieldManager.FieldTypes.REBUILT_HUB_SCORING -> Text("This is a rebuilt hub scoring at one bint 😮🎉🎉🎉")
            }
        }
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Row(
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add Data Field:",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Left,
            )
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                UserInputs.DropdownInput(
                    options = editFormsViewModel.getFieldTypes(),
                    selectedOption = "Textbox",
                    onOptionSelected = {
                        editFormsViewModel.changeSelectedFieldTypeToAdd(it)
                    }
                )
                UserInputs.OutlinedButtonInput(
                    onClick = { editFormsViewModel.addField() },
                    icon = Icons.Default.Add,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    iconColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun EditMatchFormsScreen(navController: NavController, editFormsViewModel: EditFormsViewModel) {
    // TODO: implement match forms
    Text("Edit Match Forms Screen coming soon... not done yet!! bark bark bint bint 67")
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