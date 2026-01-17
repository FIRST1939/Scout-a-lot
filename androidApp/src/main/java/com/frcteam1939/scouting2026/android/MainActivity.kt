package com.frcteam1939.scouting2026.android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.frcteam1939.scouting2026.data.AppDatabase
import com.frcteam1939.scouting2026.data.getDatabase
import com.frcteam1939.scouting2026.data.initAppContextForDB
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import com.frcteam1939.scouting2026.viewmodels.viewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Give the context to the DB initializer
        initAppContextForDB(this)
        setContent {
            MyApplicationTheme {
                ScoutingApp()
            }
        }
    }
}

object AppRoutes {
    object MatchScouting {
        const val SCOUT_MATCHES = "scout matches"
        const val TEAM_CONFIRMATION = "team confirmation"
        const val SCOUT_AUTO = "scout auto"
        const val SCOUT_TELEOP = "scout teleop"
        const val SCOUT_ENDGAME = "scout endgame"
    }

    object PitScouting {
        const val SCOUT_PITS = "scout pits"
    }
    const val MATCH_REPORTS = "match reports"
    const val TEAM_STATS = "team info"
    const val SYNC = "sync"
    const val EDIT_FORMS = "edit forms"
    const val SELECT = "select"
    const val SAVED = "saved"
    const val SETTINGS = "settings"
    const val OPERATOR = "operator"
}

// Properties specific to the Navigation Rail
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object ScoutMatches : Screen(AppRoutes.MatchScouting.SCOUT_MATCHES, "Scout Matches", Icons.Default.Build)
    object ScoutPits : Screen(AppRoutes.PitScouting.SCOUT_PITS, "Scout Pits", Icons.Default.Person)
    object MatchReports : Screen(AppRoutes.MATCH_REPORTS, "Match Reports", Icons.Default.Info)
    object TeamStats : Screen(AppRoutes.TEAM_STATS, "Team Stats", Icons.Default.List)
    object Sync : Screen(AppRoutes.SYNC, "Sync", Icons.Default.Refresh)
    object EditForms : Screen(AppRoutes.EDIT_FORMS, "Edit Forms", Icons.Default.Edit)
    object Settings : Screen(AppRoutes.SETTINGS, "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//navigation controller
fun ScoutingApp() {
    val database: AppDatabase = remember { getDatabase() }

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // List of items to show in the NavigationRail
    val items = listOf(
        Screen.ScoutMatches,
        Screen.ScoutPits,
        Screen.MatchReports,
        Screen.TeamStats,
        Screen.Sync,
        Screen.EditForms,
        Screen.Settings,
    )
    var selectedItem by remember { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primary,
            ) {
                Spacer(Modifier.height(12.dp))
                items.forEachIndexed { index, screen ->
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = index == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = index
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.onBackground,
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedContainerColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                        )
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("1939 Scouting Tool") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        navigationIconContentColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController = navController, startDestination = AppRoutes.MatchScouting.SCOUT_MATCHES) {
                    composable(AppRoutes.MatchScouting.SCOUT_MATCHES) {
                        ScoutMatchesScreen(navController = navController)
                    }
                    composable(AppRoutes.MatchScouting.TEAM_CONFIRMATION) {
                        TeamConfirmationScreen(navController = navController)
                    }
                    composable(AppRoutes.MatchScouting.SCOUT_AUTO) {
                        // TODO: auto scouting screen
                    }
                    composable(AppRoutes.PitScouting.SCOUT_PITS) {
                        // TODO: pit scouting screen & implementation
                    }
                    composable(AppRoutes.MATCH_REPORTS) {
                        // TODO: match report generation
                    }
                    composable(AppRoutes.TEAM_STATS) {
                        // TODO: team stats page (all teams)
                    }
                    composable(AppRoutes.SYNC) {
                        // TODO: device & data synchronization
                    }
                    composable(AppRoutes.EDIT_FORMS) {
                        // TODO: add edit forms function
                        // this is where saved and select will be
                    }
                    composable(AppRoutes.SETTINGS) {
                        SettingsScreen(navController = navController)
                    }
                    composable(AppRoutes.OPERATOR) {
                        OperatorScreen(navController = navController)
                    }
                    composable(AppRoutes.SELECT) {
                        SelectScreen(navController = navController)
                    }
                    composable(AppRoutes.SAVED) {
                        SavedScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
//this is scouting screen
fun ScoutMatchesScreen(navController: NavController) {
    var matchNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text("Current Event: None Selected")
        Image(
            painter = painterResource(id = R.drawable.bunny),
            contentDescription = "bunny"
        )
        UserInputs.TextButtonInput(
            onClick = {
                var canStartScouting = true
                if (matchNumber.toIntOrNull() == null) {
                    canStartScouting = false
                    makeText(context, "Please enter a valid match number!", Toast.LENGTH_SHORT).show()
                }

                if (canStartScouting) navController.navigate(AppRoutes.MatchScouting.TEAM_CONFIRMATION)
            },
            text = "Scout Match",
        )

        //input for match selector
        UserInputs.TextboxInput(
            value = matchNumber,
            onValueChange = { matchNumber = it },
            label = "Match Number",
            singleLine = true,
            forceNumberInput = true,
            characterLimit = 3
        )
    }
}

@Composable
fun TeamConfirmationScreen(navController: NavController) {
    // TODO: add photo capability
    val teamNumber = 1234
    var noShowPopup by remember { mutableStateOf(false) }

    if(noShowPopup) {
        AlertDialog(
            onDismissRequest = { noShowPopup = false },
            title = { Text("Why did the robot not show up?", color = MaterialTheme.colorScheme.onPrimary) },
            containerColor = MaterialTheme.colorScheme.primary,

            // Robot no-showed
            confirmButton = {
                UserInputs.TextButtonInput(
                    onClick = {
                        noShowPopup = false
                        navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
                    },
                    text = "Robot no-show",
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },

            // Robot not part of this match
            dismissButton = {
                UserInputs.TextButtonInput(
                    onClick = {
                        noShowPopup = false
                        navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
                    },
                    text = "Team not in match",
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.missing_image),
            contentDescription = "no robot image available",
            modifier = Modifier.padding(all = 50.dp)
        )
        Text(text = "You're scouting team $teamNumber", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Can you locate this robot on the field?")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Yes button
            UserInputs.TextButtonInput(
                onClick = { navController.navigate(AppRoutes.MatchScouting.SCOUT_AUTO) },
                text = "Yes",
                modifier = Modifier.padding(all = 25.dp),
                containerColor = MaterialTheme.colorScheme.primary
            )

            // No button
            UserInputs.TextButtonInput(
                onClick = { noShowPopup = true },
                text = "No",
                modifier = Modifier.padding(all = 25.dp),
            )
        }
    }
}

@Composable
//main settings
fun SettingsScreen(navController: NavController) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = viewModelFactory { SettingsViewModel() }
    )
    // This collects the StateFlow from the settings view model
    val settingsState by settingsViewModel.settings.collectAsState()
    val currentSettings = settingsState ?: return

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "1939 Scouting Tool - Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        UserInputs.SwitchTextBarInput(
            checked = currentSettings.exampleSetting,
            onCheckedChange = {
                settingsViewModel.updateExampleSetting(it)
            },
            text = "Example Setting (does nothing)"
        )
        UserInputs.DropdownTextBarInput(
            options = listOf("Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3"),
            selectedOption = currentSettings.deviceRobotAssignment,
            onOptionSelected = {
                settingsViewModel.updateDeviceRobotAssignment(it)
            },
            text = "Robot and Alliance Assignment"
        )
        UserInputs.TextButtonInput(
            onClick = {navController.navigate(AppRoutes.OPERATOR)},
            text = "Operator Settings",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 30.dp),
        )
    }
}

@Composable
//operator settings menu
fun OperatorScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is for operators.. One pint! 😂😂", style= MaterialTheme.typography.headlineMedium)
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
        UserInputs.TextButtonInput(
            onClick = { navController.popBackStack() },
            text = "Go Back"
        )
    }
}

@Composable
//shows all created sets to manage
fun SavedScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You can see and manage saved ones . . at one pint", style= MaterialTheme.typography.headlineMedium)
        UserInputs.TextButtonInput(
            onClick = { navController.popBackStack() },
            text = "Go Back"
        )
    }
}

@Composable
//this is where you select a set that the scouter will use
fun SelectScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You will select here. . at one pint")
        UserInputs.TextButtonInput(
            onClick = { navController.popBackStack() },
            text = "Go Back"
        )
    }
}
