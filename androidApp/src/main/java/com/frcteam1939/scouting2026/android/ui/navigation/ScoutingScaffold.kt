package com.frcteam1939.scouting2026.android.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScoutingScaffold(
    navController: NavController,
    content: @Composable () -> Unit,
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
                val items = listOf(
                    Screen.ScoutMatches,
                    Screen.ScoutPits,
                    Screen.Settings,
                )
                val selectedItem = items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = index == selectedItem,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(Modifier.padding(innerPadding)) { content() }
    }
}