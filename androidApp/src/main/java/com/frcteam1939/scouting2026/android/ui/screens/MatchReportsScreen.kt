package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.viewmodels.DataViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp

@Composable
fun MatchReportsScreen(navController: NavController, dataViewModel: DataViewModel) {
    var matchList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        matchList = dataViewModel.getMatches()
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        matchList.forEach { match ->

            UserInputs.OutlinedButtonTextBarInput(
                text = match,
                buttonText = "",
                icon = Icons.Default.ArrowForward,
                onClick = {
                    dataViewModel.changeCurrentMatch(match)
                    navController.navigate(AppRoutes.MatchReports.VIEW_MATCH_REPORT)
                }
            )
        }
    }
}

@Composable
fun ViewMatchReportScreen(navController: NavController, dataViewModel: DataViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
    ) {
        Text(
            text = dataViewModel.currentMatch.collectAsState().value,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(all = 16.dp)
        )
    }
}