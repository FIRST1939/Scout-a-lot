package com.frcteam1939.scouting2026.android

import android.app.Activity
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.frcteam1939.scouting2026.data.getDatabase
import com.frcteam1939.scouting2026.initAppContext
import com.frcteam1939.scouting2026.setActivity
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import com.frcteam1939.scouting2026.viewmodels.getViewModelFactory
import kotlinx.coroutines.launch
import android.net.Uri
import android.content.Intent
import android.provider.DocumentsContract
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Give the context to the business logic
        initAppContext(this)
        setContent {
            MyApplicationTheme {
                ScoutingApp()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setActivity(this)
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
    const val EDIT_EVENTS = "edit events"
    const val ADD_EVENT = "add event"
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
    val context = LocalContext.current

    // Create a launcher to handle the Bluetooth enable request
    val enableBluetoothLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Logic for when the user clicks 'Allow' or 'Deny' on the system Bluetooth popup
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Bluetooth is running..", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Bluetooth is off. Sync may not work..", Toast.LENGTH_SHORT).show()
        }
    }

    val bluetoothManager: BluetoothManager? = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[android.Manifest.permission.BLUETOOTH_CONNECT] ?: false
        if (isGranted) {
            // Now that we have permission, we can try to enable Bluetooth
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
        } else {
            Toast.makeText(context, "Bluetooth permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        if (bluetoothAdapter != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Android 12+ (API 31+): Must request BLUETOOTH_CONNECT via dialog
                permissionLauncher.launch(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT))
            } else {
                // Android 11 and lower: Permissions are granted automatically if in Manifest.
                // Just check if BT is on, and if not, show the "Enable Bluetooth" system dialog.
                try {
                    if (!bluetoothAdapter.isEnabled) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        enableBluetoothLauncher.launch(enableBtIntent)
                    }
                } catch (e: Exception) {
                    // Safety check for devices with restricted profiles
                    Toast.makeText(context, "Could not open Bluetooth settings", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
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
                        SyncScreen(navController = navController)
                    }
                    composable(AppRoutes.EDIT_FORMS) {
                        // TODO: add edit forms function
                        // this is where saved and select will be
                    }
                    composable(AppRoutes.SETTINGS) {
                        SettingsScreen(navController = navController)
                    }
                    composable(AppRoutes.EDIT_EVENTS) {
                        EditEventsScreen(navController = navController)
                    }
                    composable(AppRoutes.ADD_EVENT) {
                        AddEventScreen(navController = navController)
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

fun startActivityForResult(activity: Intent, intent2: Any) {}

fun getSystemService(context: Context): BluetoothManager?{
    return context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
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
fun SyncScreen(navController: NavController) {
    fun startActivity(context: Intent) {}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sync",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        UserInputs.TextButtonInput(
            onClick = {
                fun openBluetoothSettings() {
                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                startActivity(intent)
            }},
            text = "Send"

        )
        TextButton(
            onClick = {}
        ) {
            Text(text = "Recieve")
        }
    }
}

const val PICK_PDF_FILE = 2
@Composable
fun openFile(pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
    }
    //startActivityForResult(intent, PICK_PDF_FILE)
}

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val database = remember(context) {
        initAppContext(context)
        getDatabase()
    }
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = getViewModelFactory()
    )
    // This collects the StateFlow from the settings view model
    val settingsState by settingsViewModel.settings.collectAsState()
    val currentSettings = settingsState ?: return

    // Get the event names
    val eventNames by settingsViewModel
        .getEventNames()
        .collectAsState(initial = emptyList())
    val eventNamesWithNoneOption = listOf("None Selected").plus(eventNames)
    val selectedEvent = currentSettings.currentEvent.ifBlank { "None Selected" }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        )

        // Select example setting
        UserInputs.SwitchTextBarInput(
            checked = currentSettings.exampleSetting,
            onCheckedChange = {
                settingsViewModel.updateExampleSetting(it)
            },
            text = "Example Setting (does nothing)"
        )

        // Select robot assignment
        UserInputs.DropdownTextBarInput(
            options = listOf("Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3"),
            selectedOption = currentSettings.deviceRobotAssignment,
            onOptionSelected = {
                settingsViewModel.updateDeviceRobotAssignment(it)
            },
            text = "Robot and Alliance Assignment"
        )

        // Select current event
        UserInputs.DropdownTextBarInput(
            options = eventNamesWithNoneOption,
            selectedOption = selectedEvent,
            onOptionSelected = { selected ->
                settingsViewModel.updateCurrentEvent(
                    if (selected == "None Selected") "" else selected
                )
            },
            text = "Current Event"
        )

        Text(
            text = "Actions:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        )

        // Add event
        UserInputs.TextButtonInput(
            onClick = { navController.navigate(AppRoutes.EDIT_EVENTS) },
            text = "Edit Events",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 4.dp),
        )

        // Navigate to operator settings
        UserInputs.TextButtonInput(
            onClick = { navController.navigate(AppRoutes.OPERATOR) },
            text = "Operator Settings",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 4.dp),
        )
    }
}

@Composable
fun EditEventsScreen(navController: NavController) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = getViewModelFactory()
    )
    val eventNames by settingsViewModel
        .getEventNames()
        .collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInputs.OutlinedButtonTextBarInput(
            text = "Add Event",
            buttonText = "",
            icon = Icons.Default.Add,
            onClick = {
                navController.navigate(AppRoutes.ADD_EVENT)
            }
        )
        UserInputs.OutlinedButtonTextBarInput(
            text = "Refresh Match Info",
            buttonText = "",
            icon = Icons.Default.Refresh,
            onClick = {
                coroutineScope.launch {
                    eventNames.forEach {
                        val event = settingsViewModel.getEventByName(it)

                        settingsViewModel.updateEventMatchInfo(
                            // TODO: look into !!. operator -- do we need error handling?
                            name = event!!.customEventName,
                            code = event.eventCode,
                            year = event.eventYear
                        )
                    }
                }
            }
        )
        Text(
            text = "Your Events:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        )
        eventNames.forEach {
            var qualsLoaded by remember { mutableStateOf(false) }
            var playoffsLoaded by remember { mutableStateOf(false) }

            LaunchedEffect(it) {
                qualsLoaded = settingsViewModel.checkQualsMatchLoadStatus(it)
                playoffsLoaded = settingsViewModel.checkPlayoffMatchLoadStatus(it)
            }

            UserInputs.OutlinedButtonTextBarInput(
                text = it,
                buttonText = "",
                icon = Icons.Default.Delete,
                onClick = {
                    settingsViewModel.deleteEvent(it)
                    if (it == settingsViewModel.settings.value?.currentEvent) {
                        settingsViewModel.updateCurrentEvent("")
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (qualsLoaded) {
                    Text(
                        text = "Quals Matches: Loaded!",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                else {
                    Text(
                        text = "Quals Matches: Not Loaded.",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                if (playoffsLoaded) {
                    Text(
                        text = "Playoff Matches: Loaded!",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                else {
                    Text(
                        text = "Playoff Matches: Not Loaded.",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun AddEventScreen(navController: NavController) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = getViewModelFactory()
    )

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var eventName by remember { mutableStateOf("") }
    var eventCode by remember { mutableStateOf("") }
    var eventYear by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Event:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        )

        // Prompt event name
        UserInputs.TextboxInput(
            value = eventName,
            onValueChange = { eventName = it },
            label = "Event Name",
            singleLine = true
        )

        // Prompt event code
        UserInputs.TextboxInput(
            value = eventCode,
            onValueChange = { eventCode = it },
            label = "Event Code",
            singleLine = true
        )

        // Prompt event year
        UserInputs.TextboxInput(
            value = eventYear,
            onValueChange = { eventYear = it },
            label = "Event Year",
            singleLine = true,
            forceNumberInput = true,
            characterLimit = 4
        )

        UserInputs.TextButtonInput(
            onClick = {
                coroutineScope.launch {
                    val isValid = !eventName.isBlank()
                                && !eventCode.isBlank()
                                && !eventYear.isBlank()
                                && settingsViewModel.checkEventValidity(eventCode, eventYear.toInt())?: false
                    if (isValid) {
                        settingsViewModel.createEvent(eventName, eventCode, eventYear.toInt())
                        navController.popBackStack()
                        makeText(context, "Event creation successful!", Toast.LENGTH_SHORT).show()
                    }
                    else
                        makeText(context, "Invalid event info", Toast.LENGTH_SHORT).show()
                }
            },
            text = "Add Event"
        )
    }
}

@Composable
//operator settings menu
fun OperatorScreen(navController: NavController) {
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
