package com.frcteam1939.scouting2026.android

import com.frcteam1939.scouting2026.android.ui.navigation.AppNavigationHost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.frcteam1939.scouting2026.android.ui.MyApplicationTheme
import com.frcteam1939.scouting2026.initAppContext
import com.frcteam1939.scouting2026.setActivity
import com.frcteam1939.scouting2026.android.sync.bluetooth.InitBT

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    InitBT()

    // NaviHost
    AppNavigationHost(navController, drawerState, scope)
}