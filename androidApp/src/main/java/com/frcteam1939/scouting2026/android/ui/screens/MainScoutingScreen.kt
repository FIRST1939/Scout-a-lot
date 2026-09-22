package com.frcteam1939.scouting2026.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.frcteam1939.scouting2026.android.ui.templates.ButtonBarTemplate
import com.frcteam1939.scouting2026.android.ui.templates.CheckboxFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.DropdownFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.RebuiltHubScoringFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.RebuiltStartingPositionTemplate
import com.frcteam1939.scouting2026.android.ui.templates.SwitchFieldTemplate
import com.frcteam1939.scouting2026.android.ui.templates.TextboxFieldTemplate
import com.frcteam1939.scouting2026.feature.forms.model.ButtonBar
import com.frcteam1939.scouting2026.feature.forms.model.Checkbox
import com.frcteam1939.scouting2026.feature.forms.model.Dropdown
import com.frcteam1939.scouting2026.feature.forms.model.RebuiltHubScoring
import com.frcteam1939.scouting2026.feature.forms.model.RebuiltStartingPosition
import com.frcteam1939.scouting2026.feature.forms.model.Switch
import com.frcteam1939.scouting2026.feature.forms.model.Textbox
import com.frcteam1939.scouting2026.feature.scouting.ScoutingViewModel
import kotlin.collections.contains

@Composable
fun MatchScoutingBottomBar(navController: NavController, scoutingViewModel: ScoutingViewModel, currentRoute: String?) {
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
                    scoutingViewModel.changeCurrentPage(newCurrentPage = label)
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    // TODO: fix this color bruh (shouldn't be an absolute color)
                    selectedIconColor = Color(0xFF000000),
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun MatchScoutingScaffold(
    navController: NavController,
    scoutingViewModel: ScoutingViewModel,
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
                MatchScoutingBottomBar(navController, scoutingViewModel, currentRoute)
            }
        }
    ) { innerPadding -> content(innerPadding) }
}

@Composable
fun ScoutAutoScreen(scoutingViewModel: ScoutingViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(text = "Scouting Auto", style = MaterialTheme.typography.headlineLarge)
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
        RebuiltStartingPositionTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = RebuiltStartingPosition(
                id = 1,
                label = "Starting Position"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 2,
                label = "Robot is preloaded?"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 3,
                label = "Robot picks up from...",
                options = listOf("Depot", "Outpost", "Alliance Zone (Ground)", "Neutral Zone")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        RebuiltHubScoringFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = RebuiltHubScoring(
                id = 4,
                label = "Hub Scoring"
            ),
            auto = true
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 5,
                label = "Robot climbs tower?"
            )
        )
    }
}

@Composable
fun ScoutTeleopScreen(scoutingViewModel: ScoutingViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(text = "Scouting Teleop", style = MaterialTheme.typography.headlineLarge)
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 0,
                label = "Travel",
                options = listOf("Under trench?", "Over bump?")
            )
        )
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 1,
                label = "Difficulty travelling (explain in qualitative)"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 2,
                label = "Plays defense"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        CheckboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Checkbox(
                id = 3,
                label = "Robot picks up from...",
                options = listOf("Depot", "Outpost", "Ground")
            )
        )
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 4,
                label = "Pushes fuel into outpost"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        ButtonBarTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = ButtonBar(
                id = 5,
                label = "Ferry volume",
                options = listOf("Low", "Medium", "High", "Very High")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        RebuiltHubScoringFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = RebuiltHubScoring(
                id = 6,
                label = "Hub Scoring"
            ),
            auto = false
        )
    }
}

@Composable
fun ScoutEndgameScreen(navController: NavController, scoutingViewModel: ScoutingViewModel, modifier: Modifier = Modifier) {
    // TODO: add endgame scouting screen
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        Text(text = "Scouting Teleop", style = MaterialTheme.typography.headlineLarge)
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        DropdownFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Dropdown(
                id = 0,
                label = "Tower level",
                options = listOf("Did not climb", "Level 1", "Level 2", "Level 3")
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        SwitchFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Switch(
                id = 1,
                label = "Robot died during match?"
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        TextboxFieldTemplate(
            scoutingViewModel = scoutingViewModel,
            fieldContent = Textbox(
                id = 2,
                label = "Qualitative notes",
                singleLine = false,
                forceNumberInput = false
            )
        )
        Divider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)
        UserInputs.TextButtonInput(
            onClick = {
                scoutingViewModel.saveMatchData()
                navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
            },
            text = "Submit Match Data",
            modifier = Modifier.padding(all = 32.dp)
        )
    }
}
