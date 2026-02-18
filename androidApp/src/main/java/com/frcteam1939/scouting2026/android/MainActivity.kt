package com.frcteam1939.scouting2026.android

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.frcteam1939.scouting2026.android.ui.MyApplicationTheme
import com.frcteam1939.scouting2026.initAppContext
import com.frcteam1939.scouting2026.setActivity
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import com.frcteam1939.scouting2026.viewmodels.getViewModelFactory
import com.frcteam1939.scouting2026.android.ui.AppRoutes
import com.frcteam1939.scouting2026.android.ui.screens.AddEventScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditEventsScreen
import com.frcteam1939.scouting2026.android.ui.screens.MatchScoutingScaffold
import com.frcteam1939.scouting2026.android.ui.screens.OperatorScreen
import com.frcteam1939.scouting2026.android.ui.screens.SavedScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutAutoScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutEndgameScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutMatchesScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutPitForm
import com.frcteam1939.scouting2026.android.ui.screens.ScoutPitScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutTeleopScreen
import com.frcteam1939.scouting2026.android.ui.screens.SelectScreen
import com.frcteam1939.scouting2026.android.ui.screens.SettingsScreen
import com.frcteam1939.scouting2026.android.ui.screens.SyncScreen
import com.frcteam1939.scouting2026.android.ui.screens.TeamConfirmationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Give the context to the business logic
        initAppContext(this)
        setContent { MyApplicationTheme { ScoutingApp() } }
    }
    override fun onResume() {
        super.onResume()
        setActivity(this)
    }
}

// Get scoutingViewModel based on location
@Composable
fun getScoutingViewModel(
        backStackEntry: NavBackStackEntry,
        navController: NavController,
        route: String
): ScoutingViewModel {
    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(route) }
    return viewModel(viewModelStoreOwner = parentEntry, factory = getViewModelFactory())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// navigation controller
fun ScoutingApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Initialize Bluetooth
    InitBT()

    NavHost(navController = navController, startDestination = AppRoutes.MatchScouting.GRAPH) {
        navigation(
            startDestination = AppRoutes.MatchScouting.SCOUT_MATCHES,
            route = AppRoutes.MatchScouting.GRAPH
        ) {
            composable(AppRoutes.MatchScouting.SCOUT_MATCHES) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.MatchScouting.GRAPH
                    )
                ScoutMatchesScreen(
                    navController = navController,
                    scoutingViewModel = scoutingViewModel
                )
            }
            composable(AppRoutes.MatchScouting.TEAM_CONFIRMATION) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.MatchScouting.GRAPH
                    )
                TeamConfirmationScreen(
                    navController = navController,
                    scoutingViewModel = scoutingViewModel
                )
            }
            composable(AppRoutes.MatchScouting.SCOUT_AUTO) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.MatchScouting.GRAPH
                    )
                MatchScoutingScaffold(navController) { padding ->
                    ScoutAutoScreen(scoutingViewModel = scoutingViewModel)
                }
            }
            composable(AppRoutes.MatchScouting.SCOUT_TELEOP) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.MatchScouting.GRAPH
                    )
                MatchScoutingScaffold(navController) { padding ->
                    ScoutTeleopScreen(scoutingViewModel = scoutingViewModel)
                }
            }
            composable(AppRoutes.MatchScouting.SCOUT_ENDGAME) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.MatchScouting.GRAPH
                    )
                MatchScoutingScaffold(navController) { padding ->
                    ScoutEndgameScreen(
                        navController = navController,
                        scoutingViewModel = scoutingViewModel
                    )
                }
            }
        }
        navigation(
            startDestination = AppRoutes.PitScouting.SCOUT_PITS,
            route = AppRoutes.PitScouting.GRAPH
        ) {
            composable(AppRoutes.PitScouting.SCOUT_PITS) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.PitScouting.GRAPH
                    )
                ScoutPitScreen(scoutingViewModel = scoutingViewModel)
            }
            composable(AppRoutes.PitScouting.SCOUT_PITS_FORM) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(
                        backStackEntry,
                        navController,
                        AppRoutes.PitScouting.GRAPH
                    )
                ScoutPitForm(scoutingViewModel = scoutingViewModel)
            }
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
        navigation(
            startDestination = AppRoutes.Settings.SETTINGS,
            route = AppRoutes.Settings.GRAPH
        ) {
            composable(AppRoutes.Settings.SETTINGS) { backStackEntry ->
                val parentEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(AppRoutes.Settings.GRAPH)
                    }
                val settingsViewModel: SettingsViewModel =
                    viewModel(
                        viewModelStoreOwner = parentEntry,
                        factory = getViewModelFactory()
                    )
                SettingsScreen(
                    navController = navController,
                    settingsViewModel = settingsViewModel
                )
            }
            composable(AppRoutes.Settings.EDIT_EVENTS) { backStackEntry ->
                val parentEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(AppRoutes.Settings.GRAPH)
                    }
                val settingsViewModel: SettingsViewModel =
                    viewModel(
                        viewModelStoreOwner = parentEntry,
                        factory = getViewModelFactory()
                    )
                EditEventsScreen(
                    navController = navController,
                    settingsViewModel = settingsViewModel
                )
            }
            composable(AppRoutes.Settings.ADD_EVENT) { backStackEntry ->
                val parentEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(AppRoutes.Settings.GRAPH)
                    }
                val settingsViewModel: SettingsViewModel =
                    viewModel(
                        viewModelStoreOwner = parentEntry,
                        factory = getViewModelFactory()
                    )
                AddEventScreen(
                    navController = navController,
                    settingsViewModel = settingsViewModel
                )
            }
        }
        composable(AppRoutes.OPERATOR) { OperatorScreen(navController = navController) }
        composable(AppRoutes.SELECT) { SelectScreen(navController = navController) }
        composable(AppRoutes.SAVED) { SavedScreen(navController = navController) }
    }
}

@Composable
fun InitBT() {
    val context = LocalContext.current
    // Create a launcher to handle the Bluetooth enable request
    val enableBluetoothLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // Logic for when the user clicks 'Allow' or 'Deny' on the system Bluetooth popup
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Bluetooth is running..", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Bluetooth is off. Sync may not work..",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    val bluetoothManager: BluetoothManager? =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = permissions[android.Manifest.permission.BLUETOOTH_CONNECT] ?: false
            if (isGranted) {
                // Now that we have permission, we can try to enable Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothLauncher.launch(enableBtIntent)
            } else {
                Toast.makeText(context, "Bluetooth permission denied", Toast.LENGTH_SHORT)
                    .show()
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
                    Toast.makeText(context, "Could not open Bluetooth settings", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}