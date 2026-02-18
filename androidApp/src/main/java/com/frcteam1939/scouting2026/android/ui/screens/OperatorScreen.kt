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
import com.frcteam1939.scouting2026.android.ui.AppRoutes
import com.frcteam1939.scouting2026.android.ui.UserInputs


@Composable
// operator settings menu
fun OperatorScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "This is for operators.. One pint! 😂😂",
            style = MaterialTheme.typography.headlineMedium
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserInputs.TextButtonInput(
                onClick = {},
                text = "Create a set",
                modifier = Modifier.padding(all = 10.dp),
            )
            UserInputs.TextButtonInput(
                onClick = { navController.navigate(AppRoutes.SELECT) },
                text = "Edit Forms",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = "Selected: sigmaboi2026 (match scout)")
            UserInputs.TextButtonInput(
                onClick = { navController.navigate(AppRoutes.SAVED) },
                text = "Saved Sets",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        UserInputs.TextButtonInput(onClick = { navController.popBackStack() }, text = "Go Back")
    }
}

@Composable
// shows all created sets to manage
fun SavedScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "You can see and manage saved ones . . at one pint",
            style = MaterialTheme.typography.headlineMedium
        )
        UserInputs.TextButtonInput(onClick = { navController.popBackStack() }, text = "Go Back")
    }
}

@Composable
// this is where you select a set that the scouter will use
fun SelectScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You will select here. . at one pint")
        UserInputs.TextButtonInput(onClick = { navController.popBackStack() }, text = "Go Back")
    }
}