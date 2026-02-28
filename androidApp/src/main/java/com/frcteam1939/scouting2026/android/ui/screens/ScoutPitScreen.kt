package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.frcteam1939.scouting2026.android.ui.templates.ButtonBarTemplate
import com.frcteam1939.scouting2026.android.ui.templates.TextboxFieldTemplate
import com.frcteam1939.scouting2026.data.structuring.ButtonBar
import com.frcteam1939.scouting2026.data.structuring.Textbox
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel

@Composable
fun ScoutPitScreen(scoutingViewModel: ScoutingViewModel, navController: NavController) {
    var teamList by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(true) {
        teamList = scoutingViewModel.getTeamList()
    }
    Column(
        verticalArrangement = Arrangement.Top
    ) {
        teamList.forEach { team ->
            UserInputs.OutlinedButtonTextBarInput(
                text = team,
                buttonText = String(),
                onClick = {
                    navController.navigate(AppRoutes.PitScouting.SCOUT_PITS_FORM)
                    scoutingViewModel.changeSelectedTeam(team)
                },
                icon = Icons.Default.ArrowForward,
                textColor = MaterialTheme.colorScheme.onBackground
            )
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
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 1,
                label = "Approximate Fuel Capacity",
                singleLine = true,
                forceNumberInput = true,
                characterLimit = 3
            )
        )
    }
}