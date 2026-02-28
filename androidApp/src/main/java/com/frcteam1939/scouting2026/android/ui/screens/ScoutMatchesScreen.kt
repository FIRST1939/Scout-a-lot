package com.frcteam1939.scouting2026.android.ui.screens

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.R
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import kotlin.text.ifEmpty

@Composable
// This is the first scouting screen
fun ScoutMatchesScreen(navController: NavController, scoutingViewModel: ScoutingViewModel) {
    val settingsState by scoutingViewModel.settings.collectAsState()
    val currentSettings = settingsState ?: return
    val currentEvent = currentSettings.currentEvent
    val matchNumber = scoutingViewModel.matchNumber
    var currentForm by remember { mutableStateOf("") }

    LaunchedEffect(currentEvent) {
        if (currentEvent.isNotEmpty()) {
            currentForm = scoutingViewModel.getCurrentForm(currentEvent, formType = "match")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(
            text = "Current Event: ${currentEvent.ifEmpty { "None Selected" }}",
            modifier = Modifier.padding(all = 8.dp)
        )
        Text(
            text = "Current Form: ${currentForm.ifEmpty { "None Selected" }}",
            modifier = Modifier.padding(all = 8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bunny),
            contentDescription = "bunny"
        )
        UserInputs.TextButtonInput(
            onClick = {
                // Check if scouting is allowed to happen
                var canStartScouting = true
                if (currentEvent.isEmpty()) {
                    canStartScouting = false
                    makeText(context, "Please select an event to start scouting!", Toast.LENGTH_SHORT).show()
                } else if (currentForm.isEmpty()) {
                    // canStartScouting = false
                    makeText(context, "Please select a match form to start scouting!", Toast.LENGTH_SHORT).show()
                } else if (matchNumber.value.toIntOrNull() == null) {
                    canStartScouting = false
                    makeText(context, "Please enter a valid match number!", Toast.LENGTH_SHORT).show()
                }

                if (canStartScouting) navController.navigate(AppRoutes.MatchScouting.TEAM_CONFIRMATION)
            },
            text = "Scout Match",
        )

        //input for match selector
        UserInputs.TextboxInput(
            value = matchNumber.collectAsState().value,
            onValueChange = { scoutingViewModel.changeMatchNumber(it) },
            label = "Match Number",
            singleLine = true,
            forceNumberInput = true,
            characterLimit = 3
        )
    }
}


@Composable
fun TeamConfirmationScreen(navController: NavController, scoutingViewModel: ScoutingViewModel) {
    // TODO: add photo capability
    val matchNumber by scoutingViewModel.matchNumber.collectAsState()
    var teamNumber by remember { mutableStateOf("") }
    LaunchedEffect(matchNumber) { teamNumber = scoutingViewModel.getCurrentTeam() }

    var noShowPopup by remember { mutableStateOf(false) }

    if (noShowPopup) {
        AlertDialog(
            onDismissRequest = { noShowPopup = false },
            title = {
                Text(
                    "Why did the robot not show up?",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            containerColor = MaterialTheme.colorScheme.primary,

            // Robot no-showed
            confirmButton = {
                UserInputs.TextButtonInput(
                    onClick = {
                        noShowPopup = false
                        navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
                    },
                    text = "Robot no-show",
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },

            // Robot not part of this match
            dismissButton = {
                UserInputs.TextButtonInput(
                    onClick = {
                        noShowPopup = false
                        navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
                    },
                    text = "Team not in match",
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.missing_image),
            contentDescription = "no robot image available",
            modifier = Modifier.padding(all = 50.dp)
        )
        Text(
            text = "You're scouting team $teamNumber",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(text = "Can you locate this robot on the field?")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            // Yes button
            UserInputs.TextButtonInput(
                onClick = { navController.navigate(AppRoutes.MatchScouting.SCOUT_AUTO) },
                text = "Yes",
                modifier = Modifier.padding(all = 25.dp),
                containerColor = MaterialTheme.colorScheme.primary
            )

            // No button
            UserInputs.TextButtonInput(
                onClick = { noShowPopup = true },
                text = "No",
                modifier = Modifier.padding(all = 25.dp),
            )
        }
    }
}