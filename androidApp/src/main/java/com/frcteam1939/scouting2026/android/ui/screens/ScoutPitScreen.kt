package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.templates.CheckboxFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.TextboxFieldTemplate
import com.frcteam1939.scouting2026.data.structuring.Checkbox
import com.frcteam1939.scouting2026.data.structuring.Textbox
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.frcteam1939.scouting2026.android.ui.WarningBox
import kotlinx.coroutines.launch

@Composable
fun ScoutPitScreen(scoutingViewModel: ScoutingViewModel, navController: NavController) {
    var teamList by remember { mutableStateOf<List<String>>(emptyList()) }
    val deviceRobotAssignment = scoutingViewModel.settings.collectAsState().value?.deviceRobotAssignment
    val robotAssignmentColor = when (deviceRobotAssignment?.take(3)) {
        "Red" -> Color.Red
        "Blu" -> Color.Blue
        else -> MaterialTheme.colorScheme.error
    }

    LaunchedEffect(true) {
        teamList = scoutingViewModel.getPitTeamList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pit scouting assignments for ",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = deviceRobotAssignment ?: "Error",
                style = MaterialTheme.typography.headlineMedium,
                color = robotAssignmentColor
            )
        }
        Divider(thickness = 1.dp)
        if (teamList.isEmpty()) {
            WarningBox("Could not generate pit scouting assignments. Have you created and selected an event?")
        }
        teamList.forEach { team ->
            var pitDataStatus by remember { mutableStateOf("Fetching...") }
            coroutineScope.launch { pitDataStatus = scoutingViewModel.getPitDataStatus(team) }

            var teamName by remember { mutableStateOf("") }
            coroutineScope.launch { teamName = scoutingViewModel.getTeamNameByNumber(team) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$team - $teamName",
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // pit form status indicator
                    val pitDataStatusColor = when (pitDataStatus) {
                        "Fetching..." -> Color(0xFFFFC107)
                        "Not Started" -> Color(0xFFD50000)
                        "Incomplete" -> Color(0xFFFFC107)
                        "Complete" -> Color(0xFF078502)
                        else -> Color(0xFFD50000)
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = pitDataStatusColor,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .padding(ButtonDefaults.ContentPadding)
                    ) {
                        Text(
                            text = pitDataStatus,
                            color = Color.White
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            scoutingViewModel.resetFormJsons()
                            scoutingViewModel.changeSelectedTeam(team)
                            scoutingViewModel.changeCurrentPage(newCurrentPage = "Pit")
                            coroutineScope.launch {
                                scoutingViewModel.loadPreviousPitData()
                            }
                            navController.navigate(AppRoutes.PitScouting.SCOUT_PITS_FORM)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = ""
                        )
                    }
                }
            }
            Divider(thickness = 1.dp)
        }
    }
}

@Composable
fun ScoutPitForm(scoutingViewModel: ScoutingViewModel, navController: NavController) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInputs.OutlinedButtonTextBarInput(
            text = scoutingViewModel.selectedTeam.collectAsState().value,
            buttonText = String(),
            onClick = {
                scoutingViewModel.savePitData(scoutingViewModel.selectedTeam.value)
                navController.navigate(AppRoutes.PitScouting.SCOUT_PITS)
            },
            icon = Icons.Default.Check,
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 0,
                label = "Scouter Name",
                singleLine = true,
                forceNumberInput = false
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 1,
                label = "Robot picks up from...",
                options = listOf("Depot", "Outpost", "Alliance Zone", "Neutral Zone")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Approximate Fuel Capacity",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 2,
                label = "Type here",
                singleLine = true,
                forceNumberInput = true,
                characterLimit = 3
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 3,
                label = "Where can the robot travel",
                options = listOf("Under trench", "Over bump")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Robot's Drivetrain",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 4,
                label = "Type here",
                singleLine = true,
                forceNumberInput = false
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 5,
                label = "What tower levels can it climb?",
                options = listOf("1", "2", "3")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Auto Capabilities?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 6,
                label = "Type here",
                singleLine = false,
                forceNumberInput = false
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Any other notes go here:",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 7,
                label = "Type here",
                singleLine = false,
                forceNumberInput = false
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 10.dp, end = 10.dp, ),
            text = "Ask the team if you can take a picture of their robot. Take it with your phone or the tablet.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold

        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 100.dp, start = 10.dp, end = 10.dp, ),
            text = "If you took it with your phone, send it in the #robopics channel in Discord with the team's number",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
            )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
    }
}