package com.frcteam1939.scouting2026.android.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreenScaffold(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
    // List of items to show in the Navigation Drawer
    val items =
        listOf(
            Screen.ScoutMatches,
            Screen.ScoutPits,
            Screen.MatchReports,
            Screen.TeamStats,
            Screen.Sync,
            Screen.EditForms,
            Screen.Settings,
        )
    var selectedItem by remember { mutableIntStateOf(0) }

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
                        modifier =
                            Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors =
                            NavigationDrawerItemDefaults.colors(
                                selectedContainerColor =
                                    MaterialTheme.colorScheme.secondary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.onBackground,
                                selectedIconColor =
                                    MaterialTheme.colorScheme.onBackground,
                                unselectedContainerColor =
                                    MaterialTheme.colorScheme.primary,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.secondary,
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.secondary,
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
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                            navigationIconContentColor =
                                MaterialTheme.colorScheme.secondary
                        )
                )
            }
        ) { innerPadding ->
            Surface(Modifier.padding(innerPadding)) { content() }
        }
    }
}