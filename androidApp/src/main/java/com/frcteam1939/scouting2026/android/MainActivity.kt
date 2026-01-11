package com.frcteam1939.scouting2026.android


import android.os.Bundle
import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch
import kotlin.text.toIntOrNull

object AppRoutes {
    const val SCOUT_MATCHES = "scout matches"
    const val SCOUT_PITS = "scout pits"
    const val MATCH_REPORTS = "match reports"
    const val TEAM_STATS = "team info"
    const val SYNC = "sync"
    const val EDIT_FORMS = "edit forms"
    const val SELECT = "select"
    const val SAVED = "saved"
    const val SETTINGS = "settings"
    const val OPERATOR = "operator"
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object ScoutMatches : Screen(AppRoutes.SCOUT_MATCHES, "Scout Matches", Icons.Default.Build)
    object ScoutPits : Screen(AppRoutes.SCOUT_PITS, "Scout Pits", Icons.Default.Person)
    object MatchReports : Screen(AppRoutes.MATCH_REPORTS, "Match Reports", Icons.Default.Info)
    object TeamStats : Screen(AppRoutes.TEAM_STATS, "Team Stats", Icons.Default.List)
    object Sync : Screen(AppRoutes.SYNC, "Sync", Icons.Default.Refresh)
    object EditForms : Screen(AppRoutes.EDIT_FORMS, "Edit Forms", Icons.Default.Edit)
    object Settings : Screen(AppRoutes.SETTINGS, "Settings", Icons.Default.Settings)
}

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
                NavHost(navController = navController, startDestination = AppRoutes.SCOUT_MATCHES) {
                    composable(AppRoutes.SCOUT_MATCHES) {
                        ScoutMatchesScreen(navController = navController)
                    }
                    composable(AppRoutes.SCOUT_PITS) {
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
                }
            }
        }
    }
}

@Composable
fun RadioButtonSingleSelection(modifier: Modifier = Modifier) {
    val radioOptions = listOf("at", "One", "Pint 😂😂✅")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MatchSelector() {
    var state by remember { mutableStateOf("") }
    TextField(
        value = state,
        onValueChange = {
            if (it.length <= 3 && it.toIntOrNull() != null || it.isEmpty()) state = it },
        singleLine = true,
        label = { Text("Match Number") },

        )
}

@Composable
//button that goes to operator settings
fun OperatorButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
@Composable
//operator settings menu
fun OperatorSettings(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is for operators.. One pint! 😂😂", style= MaterialTheme.typography.headlineMedium)
        OperatorColumn(navController)
        Button(onClick = { navController.popBackStack() }) { Text("Go Back")}
    }
}

@Composable
//goes to saved sets menu
fun SavedButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
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
        Button(onClick = { navController.popBackStack() }) { Text("Go Back") }
    }
}

@Composable
//goes to select set screen
fun SelectButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) { Text(text, style = MaterialTheme.typography.bodyLarge) }
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
        Button(onClick = { navController.popBackStack() }) { Text("Go Back") }
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
        Button2(text = "Start Scouting")
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
//main settings
fun SettingsScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style= MaterialTheme.typography.headlineMedium)
        var isChecked by remember { mutableStateOf( false ) }
        Switch(checked = isChecked, onCheckedChange = {isChecked = it})
        RadioButtonSingleSelection()
        OperatorButton(
            text = "Operator",
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(all = 30.dp),
            onClick = {navController.navigate(AppRoutes.OPERATOR)}
        )
        Button(onClick = {navController.popBackStack() }) { Text("Go Back") }
    }
}
@Composable
//button that goes to settings
fun SettingsButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.padding(vertical = 10.dp, horizontal = 10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) { Text(text, style = MaterialTheme.typography.bodyLarge) }
}

@Composable
fun OperatorColumn(navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonCounter(text = "Create a Set")
        SelectButton(
            onClick = {navController.navigate(AppRoutes.SELECT)},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Select Set"
        )
        Text(text = "Selected: sigmaboi2026 (match scout)")
        SavedButton(
            onClick = {navController.navigate(AppRoutes.SAVED)},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Saved Sets"
        )
    }
}

@Composable
//SCOUNt BUTTON!!!! BUTTON!!!! BUTTON!!!! 😂😂 accession
fun Button2(
    text: String,
    context: Context = LocalContext.current
) {
    TextButton(
        onClick = {
            makeText(
                context,
                "NOT. IMPLEMENTED. PINT. 😡😡 WATCHDOG 🅱️ARK 🅱️INT",
                Toast.LENGTH_LONG).show()
        },
        modifier = Modifier.padding(all = 75.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.small,
    ) { Text(text = text, style = MaterialTheme.typography.bodyLarge) }
}

@Composable
//create a set button
fun ButtonCounter(text: String) {
    TextButton(
        onClick = {},
        modifier = Modifier.padding(all = 10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.small
    ) { Text(text = text, style = MaterialTheme.typography.bodyLarge) }
}
