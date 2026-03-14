package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.utils.getTeamImage
import com.frcteam1939.scouting2026.viewmodels.DataViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch

@Composable
fun TeamStatsMainScreen(dataViewModel: DataViewModel, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var teamList by remember { mutableStateOf(emptyList<String>()) }

        LaunchedEffect(dataViewModel.currentEvent) {
            teamList = dataViewModel.getTeamList()
        }

        Text(
            text = "Team Stats",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            teamList.forEach { team ->
                UserInputs.OutlinedButtonTextBarInput(
                    text = team,
                    icon = Icons.Default.ArrowForward,
                    onClick = {
                        dataViewModel.changeCurrentTeam(team)
                        coroutineScope.launch {
                            dataViewModel.updateRawData(team)
                        }
                        navController.navigate(AppRoutes.TeamStats.VIEW_TEAM_STATS)
                    },
                )
            }
        }
    }
}

@Composable
fun ViewTeamStatsScreen(dataViewModel: DataViewModel, navController: NavController) {
    var autoCycleAvg by remember { mutableStateOf("Fetching...") }
    var autoHubStats: List<String> by remember { mutableStateOf(listOf("Fetching...", "Fetching...")) }
    var teleopCycleAvg by remember { mutableStateOf("Fetching...") }
    var teleopHubStats: List<String> by remember { mutableStateOf(listOf("Fetching...", "Fetching...")) }
    var approximateCapacity by remember { mutableStateOf("Fetching...") }
    var ferryVolume by remember { mutableStateOf("Fetching...") }
    var drivetrain by remember { mutableStateOf("Fetching...") }
    var defense by remember { mutableStateOf("Fetching...") }
    var trench by remember { mutableStateOf("Fetching...") }
    var bump by remember { mutableStateOf("Fetching...") }
    var qualitativeComments: List<String> by remember { mutableStateOf(listOf("Fetching...")) }
    var robotDeathCount by remember { mutableStateOf("Fetching...") }

    LaunchedEffect(dataViewModel.currentPitData.collectAsState().value) {
        dataViewModel.updateRawData(dataViewModel.currentTeam.value)
        autoCycleAvg = dataViewModel.avgAutoCycleCount()
        autoHubStats = dataViewModel.getAvgSpeedAndAccuracyAuto()
        teleopCycleAvg = dataViewModel.avgTeleopCycleCount()
        teleopHubStats = dataViewModel.getAvgSpeedAndAccuracyTeleop()
        approximateCapacity = dataViewModel.getApproximateCapacity() ?: "Unknown"
        ferryVolume = dataViewModel.getAvgFerryVolume()
        drivetrain = dataViewModel.getDrivetrain() ?: "Unknown"
        defense = when(dataViewModel.getPlayedDefense().toString()) {
            "true" -> "✅"
            "false" -> "❌"
            else -> "Unknown"
        }
        trench = when(dataViewModel.getTrenchTravel()) {
            true -> "✅"
            false -> "❌"
        }
        bump = when(dataViewModel.getBumpTravel()) {
            true -> "✅"
            false -> "❌"
        }
        qualitativeComments = dataViewModel.getQualitativeComments()
        robotDeathCount = dataViewModel.getDeadMatchCount().toString()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(all = 16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Stats for team ${dataViewModel.currentTeam.collectAsState().value}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberDrawablePainter(getTeamImage(dataViewModel.currentTeam.collectAsState().value)),
                contentDescription = "robot image",
                modifier = Modifier.weight(1f)
            )
            Column(
                modifier = Modifier.padding(all = 16.dp).weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Divider()
                Text("Auto:", modifier = Modifier.padding(all = 4.dp), style = MaterialTheme.typography.headlineMedium)
                Text("AVG Accuracy: ${autoHubStats[0]}", modifier = Modifier.padding(all = 4.dp))
                Text("AVG Speed: ${autoHubStats[1]}", modifier = Modifier.padding(all = 4.dp))
                Text("AVG Cycles: $autoCycleAvg", modifier = Modifier.padding(all = 4.dp))
                Divider()
                Text("Teleop:", modifier = Modifier.padding(all = 4.dp), style = MaterialTheme.typography.headlineMedium)
                Text("AVG Accuracy: ${teleopHubStats[0]}", modifier = Modifier.padding(all = 4.dp))
                Text("AVG Speed: ${teleopHubStats[1]}", modifier = Modifier.padding(all = 4.dp))
                Text("AVG Cycles: $teleopCycleAvg", modifier = Modifier.padding(all = 4.dp))
                Divider()
                Text("Approx. Capacity: $approximateCapacity", modifier = Modifier.padding(all = 4.dp))
                Divider()
                Text("AVG Ferry: $ferryVolume", modifier = Modifier.padding(all = 4.dp))
                Divider()
            }
        }
        Text("Drivetrain: $drivetrain", modifier = Modifier.padding(all = 4.dp))
        Text("Has played defense: $defense", modifier = Modifier.padding(all = 4.dp))
        Text("Trench: $trench", modifier = Modifier.padding(all = 4.dp))
        Text("Bump: $bump", modifier = Modifier.padding(all = 4.dp))
        Text("Qualitative comments:", modifier = Modifier.padding(all = 4.dp), style = MaterialTheme.typography.headlineMedium)
        qualitativeComments.forEach { comment ->
            Divider()
            Text(comment, modifier = Modifier.padding(all = 4.dp))
        }
        if (robotDeathCount != "0") {
            Text("Robot has died in $robotDeathCount matches", modifier = Modifier.padding(all = 4.dp), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.tertiary)
        }
    }
}