package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import kotlin.collections.contains

@Composable
fun MatchScoutingBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        val items =
            listOf(
                Triple(AppRoutes.MatchScouting.SCOUT_AUTO, "Auto", Icons.Default.FlashOn),
                Triple(
                    AppRoutes.MatchScouting.SCOUT_TELEOP,
                    "Teleop",
                    Icons.Default.VideogameAsset
                ),
                Triple(
                    AppRoutes.MatchScouting.SCOUT_ENDGAME,
                    "Endgame",
                    Icons.Default.EmojiEvents
                ),
            )

        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(AppRoutes.MatchScouting.SCOUT_AUTO) { saveState = true }
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                colors =
                    NavigationBarItemDefaults.colors(
                        // TODO: fix this color bruh (shouldn't be an absolute color)
                        selectedIconColor = Color(0xFF000000),
                        unselectedIconColor =
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor =
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
            )
        }
    }
}

@Composable
fun MatchScoutingScaffold(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in
                listOf(
                    AppRoutes.MatchScouting.SCOUT_AUTO,
                    AppRoutes.MatchScouting.SCOUT_TELEOP,
                    AppRoutes.MatchScouting.SCOUT_ENDGAME
                )
            ) {
                MatchScoutingBottomBar(navController, currentRoute)
            }
        }
    ) { innerPadding -> content(innerPadding) }
}

@Composable
fun ScoutAutoScreen(scoutingViewModel: ScoutingViewModel) {
    // TODO: add auto scouting screen
    Text("Hi... this is the auto screen... yay!!")
}

@Composable
fun ScoutTeleopScreen(scoutingViewModel: ScoutingViewModel) {
    // TODO: add teleop scouting screen
    Text("Hi... this is the teleop screen... yay!!")
}

@Composable
fun ScoutEndgameScreen(navController: NavController, scoutingViewModel: ScoutingViewModel) {
    // TODO: add endgame scouting screen
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hi... this is the endgame screen... yay!!")
        UserInputs.TextButtonInput(
            onClick = {
                // TODO: add submission of collected match data
                navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
            },
            text = "Submit Match Data",
        )
    }
}
