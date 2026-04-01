package com.frcteam1939.scouting2026.android.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.frcteam1939.scouting2026.android.ui.CupertinoTransitions
import com.frcteam1939.scouting2026.android.ui.screens.AddEventScreen
import com.frcteam1939.scouting2026.android.ui.screens.AssignFormsScreen
import com.frcteam1939.scouting2026.android.ui.screens.ChecklistScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditEventsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditFormsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditMatchFormsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditPitFormsScreen
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
import com.frcteam1939.scouting2026.android.feature.sync.FileSyncScreen
import com.frcteam1939.scouting2026.android.feature.sync.SyncScreen
import com.frcteam1939.scouting2026.android.ui.screens.TeamConfirmationScreen
import com.frcteam1939.scouting2026.android.ui.screens.TeamStatsMainScreen
import com.frcteam1939.scouting2026.android.ui.screens.ViewTeamStatsScreen
import com.frcteam1939.scouting2026.viewmodels.ChecklistViewModel
import com.frcteam1939.scouting2026.android.feature.sync.SyncViewModel
import com.frcteam1939.scouting2026.viewmodels.DataViewModel
import com.frcteam1939.scouting2026.viewmodels.EditFormsViewModel
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import com.frcteam1939.scouting2026.viewmodels.getViewModelFactory
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavigationHost(
    navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope
) {
    val topLevelRoutes =
        setOf(
            AppRoutes.MatchScouting.SCOUT_MATCHES,
            AppRoutes.PitScouting.SCOUT_PITS,
            AppRoutes.Settings.SETTINGS,
            AppRoutes.MatchReports.MATCH_REPORTS,
            AppRoutes.TeamStats.TEAM_STATS,
            AppRoutes.SYNC,
            AppRoutes.DATA_CHECKLIST,
            AppRoutes.EditForms.EDIT_FORMS,
        )

    NavHost(
        navController = navController,
        startDestination = AppRoutes.MatchScouting.GRAPH,
        enterTransition = { CupertinoTransitions.enterTransition },
        exitTransition = { CupertinoTransitions.exitTransition },
        popEnterTransition = { CupertinoTransitions.popEnterTransition },
        popExitTransition = { CupertinoTransitions.popExitTransition },
    ) {
        navigation(
            startDestination = AppRoutes.MatchScouting.SCOUT_MATCHES,
            route = AppRoutes.MatchScouting.GRAPH
        ) {
            composable(
                route = AppRoutes.MatchScouting.SCOUT_MATCHES,
                enterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                exitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.exitTransition
                },
                popEnterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                popExitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.popExitTransition
                },
            ) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MainScreenScaffold(navController, drawerState, scope) {
                    ScoutingScaffold(navController) {
                        ScoutMatchesScreen(navController, scoutingViewModel)
                    }
                }
            }
            composable(AppRoutes.MatchScouting.TEAM_CONFIRMATION) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                TeamConfirmationScreen(navController, scoutingViewModel)
            }
            composable(AppRoutes.MatchScouting.SCOUT_AUTO) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MatchScoutingScaffold(navController, scoutingViewModel) { innerPadding ->
                    ScoutAutoScreen(scoutingViewModel, Modifier.padding(innerPadding))
                }
            }
            composable(AppRoutes.MatchScouting.SCOUT_TELEOP) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MatchScoutingScaffold(navController, scoutingViewModel) { innerPadding ->
                    ScoutTeleopScreen(scoutingViewModel, Modifier.padding(innerPadding))
                }
            }
            composable(AppRoutes.MatchScouting.SCOUT_ENDGAME) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MatchScoutingScaffold(navController, scoutingViewModel) { innerPadding ->
                    ScoutEndgameScreen(navController, scoutingViewModel, Modifier.padding(innerPadding))
                }
            }
        }

        navigation(
            startDestination = AppRoutes.PitScouting.SCOUT_PITS, route = AppRoutes.PitScouting.GRAPH
        ) {
            composable(
                route = AppRoutes.PitScouting.SCOUT_PITS,
                enterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                exitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.exitTransition
                },
                popEnterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                popExitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.popExitTransition
                },
            ) { backStackEntry ->
                val scoutingViewModel =
                    getScoutingViewModel(backStackEntry, navController, AppRoutes.PitScouting.GRAPH)
                MainScreenScaffold(navController, drawerState, scope) {
                    ScoutingScaffold(navController) {
                        ScoutPitScreen(scoutingViewModel, navController)
                    }
                }
            }
            composable(AppRoutes.PitScouting.SCOUT_PITS_FORM) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.PitScouting.GRAPH
                )
                ScoutPitForm(scoutingViewModel, navController)
            }
        }
        navigation(
            startDestination = AppRoutes.MatchReports.MATCH_REPORTS, route = AppRoutes.MatchReports.GRAPH
        ) {
            composable(
                route = AppRoutes.MatchReports.MATCH_REPORTS,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
                popEnterTransition = { fadeIn(tween(300)) },
                popExitTransition = { fadeOut(tween(300)) },
            ) {
                MainScreenScaffold(navController, drawerState, scope) {
                    // TODO: match report generation
                }
            }
        }
        navigation(
            startDestination = AppRoutes.TeamStats.TEAM_STATS, route = AppRoutes.TeamStats.GRAPH
        ) {
            composable(
                route = AppRoutes.TeamStats.TEAM_STATS,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) },
                popEnterTransition = { fadeIn(tween(300)) },
                popExitTransition = { fadeOut(tween(300)) },
            ) { backStackEntry ->
                val dataViewModel = getDataViewModel(backStackEntry, navController, AppRoutes.TeamStats.GRAPH)
                MainScreenScaffold(navController, drawerState, scope) {
                    TeamStatsMainScreen(dataViewModel, navController)
                }
            }
            composable(AppRoutes.TeamStats.VIEW_TEAM_STATS) { backStackEntry ->
                val dataViewModel = getDataViewModel(backStackEntry, navController, AppRoutes.TeamStats.GRAPH)
                SubScreenScaffold(navController) {
                    ViewTeamStatsScreen(dataViewModel, navController)
                }
            }
        }
        composable(
            route = AppRoutes.SYNC,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
            popEnterTransition = { fadeIn(tween(300)) },
            popExitTransition = { fadeOut(tween(300)) },
        ) { _ ->
            // WIP
            val bluetoothViewModel = getBluetoothViewModel()
            MainScreenScaffold(navController, drawerState, scope) {
                SyncScreen(
                    syncViewModel = bluetoothViewModel,
                    onOpenFileSync = { navController.navigate(AppRoutes.FILE_SYNC) }
                )
            }
        }
        composable(AppRoutes.FILE_SYNC) { backStackEntry ->
            val scoutingViewModel = getScoutingViewModel(backStackEntry, navController, AppRoutes.FILE_SYNC)
            SubScreenScaffold(navController) {
                FileSyncScreen(scoutingViewModel)
            }
        }
        composable(AppRoutes.DATA_CHECKLIST) { backStackEntry ->
            val checklistViewModel = getChecklistViewModel(
                backStackEntry, navController, AppRoutes.DATA_CHECKLIST
            )
            MainScreenScaffold(navController, drawerState, scope) {
                ChecklistScreen(navController, checklistViewModel)
            }
        }
        navigation(startDestination = AppRoutes.EditForms.EDIT_FORMS, route = AppRoutes.EditForms.GRAPH) {
            composable(
                route = AppRoutes.EditForms.EDIT_FORMS,
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.exitTransition
                },
                popEnterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                popExitTransition = { fadeOut(tween(300)) },
            ) { backStackEntry ->
                val editFormsViewModel = getEditFormsViewModel(backStackEntry, navController, AppRoutes.EditForms.GRAPH)
                MainScreenScaffold(navController, drawerState, scope) {
                    EditFormsScreen(navController, editFormsViewModel = editFormsViewModel)
                }
            }
            composable(AppRoutes.EditForms.EDIT_PIT_FORM) { backStackEntry ->
                val editFormsViewModel = getEditFormsViewModel(backStackEntry, navController, AppRoutes.EditForms.GRAPH)
                SubScreenScaffold(navController) {
                    EditPitFormsScreen(navController, editFormsViewModel)
                }
            }
            composable(AppRoutes.EditForms.EDIT_MATCH_FORM) { backStackEntry ->
                val editFormsViewModel = getEditFormsViewModel(backStackEntry, navController, AppRoutes.EditForms.GRAPH)
                SubScreenScaffold(navController) {
                    EditMatchFormsScreen(navController, editFormsViewModel)
                }
            }
            composable(AppRoutes.EditForms.ASSIGN_FORMS) { backStackEntry ->
                val editFormsViewModel = getEditFormsViewModel(backStackEntry, navController, AppRoutes.EditForms.GRAPH)
                SubScreenScaffold(navController) {
                    AssignFormsScreen(navController, editFormsViewModel)
                }
            }
        }
        navigation(
            startDestination = AppRoutes.Settings.SETTINGS, route = AppRoutes.Settings.GRAPH
        ) {
            composable(
                route = AppRoutes.Settings.SETTINGS,
                enterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                exitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.exitTransition
                },
                popEnterTransition = {
                    if (initialState.destination.route in topLevelRoutes) fadeIn(tween(300))
                    else CupertinoTransitions.popEnterTransition
                },
                popExitTransition = {
                    if (targetState.destination.route in topLevelRoutes) fadeOut(tween(300))
                    else CupertinoTransitions.popExitTransition
                },
            ) { backStackEntry ->
                val settingsViewModel = getSettingsViewModel(backStackEntry, navController, AppRoutes.Settings.GRAPH)
                MainScreenScaffold(navController, drawerState, scope) {
                    ScoutingScaffold(navController) {
                        SettingsScreen(navController, settingsViewModel)
                    }
                }
            }
            composable(AppRoutes.Settings.EDIT_EVENTS) { backStackEntry ->
                val settingsViewModel = getSettingsViewModel(backStackEntry, navController, AppRoutes.Settings.GRAPH)
                SubScreenScaffold(navController) {
                    EditEventsScreen(navController, settingsViewModel)
                }

            }
            composable(AppRoutes.Settings.ADD_EVENT) { backStackEntry ->
                val settingsViewModel = getSettingsViewModel(backStackEntry, navController, AppRoutes.Settings.GRAPH)
                SubScreenScaffold(navController) {
                    AddEventScreen(navController, settingsViewModel)
                }
            }
            composable(AppRoutes.Settings.OPERATOR) {
                SubScreenScaffold(navController) {
                    OperatorScreen(navController)
                }
            }
            composable(AppRoutes.Settings.SELECT) {
                SubScreenScaffold(navController) {
                    SelectScreen(navController)
                }
            }
            composable(AppRoutes.Settings.SAVED) {
                SubScreenScaffold(navController) {
                    SavedScreen(navController)
                }
            }
        }
    }
}

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

@Composable
fun getDataViewModel(
    backStackEntry: NavBackStackEntry, navController: NavController, route: String
): DataViewModel {
    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(route) }
    return viewModel(viewModelStoreOwner = parentEntry, factory = getViewModelFactory())
}

@Composable
fun getChecklistViewModel(
    backStackEntry: NavBackStackEntry, navController: NavController, route: String
): ChecklistViewModel {
    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(route) }
    return viewModel(viewModelStoreOwner = parentEntry, factory = getViewModelFactory())
}

@Composable
fun getBluetoothViewModel(): SyncViewModel {
    val activity = LocalContext.current as ComponentActivity
    return viewModel(viewModelStoreOwner = activity)
}
