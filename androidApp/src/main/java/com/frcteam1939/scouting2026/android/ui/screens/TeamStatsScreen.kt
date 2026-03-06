package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.R
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.utils.getTeamImage
import com.frcteam1939.scouting2026.viewmodels.DataViewModel
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun TeamStatsMainScreen(dataViewModel: DataViewModel, navController: NavController) {

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
                        navController.navigate(AppRoutes.TeamStats.VIEW_TEAM_STATS)
                    },
                )
            }
        }
    }
}

@Composable
fun ViewTeamStatsScreen(dataViewModel: DataViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(all = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Stats for team ${dataViewModel.currentTeam.collectAsState().value}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberDrawablePainter(getTeamImage(dataViewModel.currentTeam.collectAsState().value)),
                contentDescription = "robot image",
                modifier = Modifier.weight(1f)
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(all = 16.dp).weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Divider()
                Text("Auto:", modifier = Modifier.padding(all = 4.dp), style = MaterialTheme.typography.headlineMedium)
                Text("AVG Speed:", modifier = Modifier.padding(all = 4.dp))
                Text("AVG Accuracy:", modifier = Modifier.padding(all = 4.dp))
                Divider()
                Text("Teleop:", modifier = Modifier.padding(all = 4.dp), style = MaterialTheme.typography.headlineMedium)
                Text("AVG Speed:", modifier = Modifier.padding(all = 4.dp))
                Text("AVG Accuracy:", modifier = Modifier.padding(all = 4.dp))
                Divider()
                Text("Approx. Capacity:", modifier = Modifier.padding(all = 4.dp))
                Divider()
                Text("AVG Ferry Volume:", modifier = Modifier.padding(all = 4.dp))
                Divider()
            }
        }
    }
}