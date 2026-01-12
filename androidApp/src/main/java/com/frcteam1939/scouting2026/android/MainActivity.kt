package com.frcteam1939.scouting2026.android


import android.os.Bundle
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            ModalDrawerSheet {
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
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
                    }
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text("Current Event: None Selected")
        Image(
            painter = painterResource(id = R.drawable.bunny),
            contentDescription = "bunny",
        )
        UserInputs.TextButtonInput(
            onClick = {
                makeText(context, "scouting not implemented... uh oh..!", Toast.LENGTH_SHORT).show()
                navController.navigate(AppRoutes.MatchScouting.TEAM_CONFIRMATION)
            },
            text = "Scout Match",
            modifier = Modifier.padding(all = 75.dp)
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
    // TODO: add team confirmation screen
}

@Composable
//main settings
fun SettingsScreen(navController: NavController) {
    var isChecked by remember { mutableStateOf( false ) }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style= MaterialTheme.typography.headlineMedium)
        UserInputs.SwitchInput(checked = isChecked, onCheckedChange = {isChecked = it})
        UserInputs.RadioButtonInput(
            radioOptions = listOf("one", "two", "three"),
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected
        )
        UserInputs.TextButtonInput(
            onClick = {navController.navigate(AppRoutes.OPERATOR)},
            text = "Operator Settings",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(all = 30.dp),
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

