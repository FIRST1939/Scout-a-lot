package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.ui.templates.CheckboxFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.CounterFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.DropdownFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.RadioButtonFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.RebuiltHubScoringFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.SwitchFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.TextboxFieldTemplate
import com.frcteam1939.scouting2026.data.structuring.Checkbox
import com.frcteam1939.scouting2026.data.structuring.Counter
import com.frcteam1939.scouting2026.data.structuring.Dropdown
import com.frcteam1939.scouting2026.data.structuring.RadioButton
import com.frcteam1939.scouting2026.data.structuring.RebuiltHubScoring
import com.frcteam1939.scouting2026.data.structuring.Switch
import com.frcteam1939.scouting2026.data.structuring.Textbox
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Scouting Auto", style = MaterialTheme.typography.headlineLarge)
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 0,
                label = "Test Textbox"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        RadioButtonFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = RadioButton(
                id = 1,
                label = "Test Radio Button",
                options = listOf("option 1", "option 2", "option 3")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 2,
                label = "Test Switch"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 3,
                label = "Test Checkbox",
                options = listOf("option 1", "option 2", "option 3")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CounterFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Counter(
                id = 4,
                label = "Test Counter"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        DropdownFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Dropdown(
                id = 5,
                label = "Test Dropdown",
                options = listOf("option 1", "option 2", "option 3")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        RebuiltHubScoringFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = RebuiltHubScoring(
                id = 6,
                label = "Test Rebuilt Hub Scoring"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
    }
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
