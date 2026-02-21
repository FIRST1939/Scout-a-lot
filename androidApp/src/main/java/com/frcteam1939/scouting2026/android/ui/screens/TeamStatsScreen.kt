package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TeamStatsMainScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Team Stats",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "Teams for event: okok... at one pint 😢",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(all = 16.dp)
        )

    }
}

@Composable
fun TeamStatsViewScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Stats for tem: bint 😂😂 hes back ( *︾▽︾)",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        Text(
            text = "Rookie year: ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(all = 16.dp),
        )


    }
}