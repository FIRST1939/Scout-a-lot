package com.frcteam1939.scouting2026.android.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScoutingScaffold(
        navController: NavController,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
            modifier = modifier,
    ) { innerPadding -> Surface(Modifier.padding(innerPadding)) { content() } }
}
