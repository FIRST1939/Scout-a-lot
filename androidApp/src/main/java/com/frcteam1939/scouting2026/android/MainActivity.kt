package com.frcteam1939.scouting2026.android

import android.Manifest
import android.app.Activity
import com.frcteam1939.scouting2026.android.ui.navigation.AppNavigationHost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.frcteam1939.scouting2026.android.ui.MyApplicationTheme
import com.frcteam1939.scouting2026.initAppContext
import com.frcteam1939.scouting2026.setActivity
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import com.frcteam1939.scouting2026.viewmodels.getViewModelFactory
import com.frcteam1939.scouting2026.storage.exportTextFile
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
import androidx.navigation.compose.navigation
import android.os.Build
import com.frcteam1939.scouting2026.storage.readTextFile
import com.frcteam1939.scouting2026.android.sync.bluetooth.InitBT
import com.frcteam1939.scouting2026.viewmodels.EditFormsViewModel
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Give the context to the business logic
        initAppContext(this)
        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ScoutingApp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setActivity(this)
    }
}

// Get scoutingViewModel based on location
@Composable
fun getScoutingViewModel(
    backStackEntry: NavBackStackEntry, navController: NavController, route: String
): ScoutingViewModel {
    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(route) }
    return viewModel(viewModelStoreOwner = parentEntry, factory = getViewModelFactory())
}

@Composable
fun getEditFormsViewModel(
    backStackEntry: NavBackStackEntry, navController: NavController, route: String
): EditFormsViewModel {
    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(route) }
    return viewModel(viewModelStoreOwner = parentEntry, factory = getViewModelFactory())
}

@Composable
fun getSettingsViewModel(
    backStackEntry: NavBackStackEntry, navController: NavController, route: String
): SettingsViewModel {
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

    // Initialize Bluetooth
    InitBT()

    // NaviHost
    AppNavigationHost(navController, drawerState, scope)
}