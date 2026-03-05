package com.frcteam1939.scouting2026.android.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.frcteam1939.scouting2026.android.getDataViewModel
import com.frcteam1939.scouting2026.android.getEditFormsViewModel
import com.frcteam1939.scouting2026.android.getScoutingViewModel
import com.frcteam1939.scouting2026.android.getSettingsViewModel
import com.frcteam1939.scouting2026.android.ui.screens.AddEventScreen
import com.frcteam1939.scouting2026.android.ui.screens.AssignFormsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditEventsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditFormsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditMatchFormsScreen
import com.frcteam1939.scouting2026.android.ui.screens.EditPitFormsScreen
import com.frcteam1939.scouting2026.android.ui.navigation.MainScreenScaffold
import com.frcteam1939.scouting2026.android.ui.screens.MatchScoutingScaffold
import com.frcteam1939.scouting2026.android.ui.screens.OperatorScreen
import com.frcteam1939.scouting2026.android.ui.screens.SavedScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutAutoScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutEndgameScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutMatchesScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutPitForm
import com.frcteam1939.scouting2026.android.ui.screens.ScoutPitScreen
import com.frcteam1939.scouting2026.android.ui.screens.ScoutTeleopScreen
import com.frcteam1939.scouting2026.android.ui.navigation.ScoutingScaffold
import com.frcteam1939.scouting2026.android.ui.screens.SelectScreen
import com.frcteam1939.scouting2026.android.ui.screens.SettingsScreen
import com.frcteam1939.scouting2026.android.ui.navigation.SubScreenScaffold
import com.frcteam1939.scouting2026.android.ui.screens.SyncScreen
import com.frcteam1939.scouting2026.android.ui.screens.TeamConfirmationScreen
import com.frcteam1939.scouting2026.android.ui.screens.TeamStatsMainScreen
import com.frcteam1939.scouting2026.android.ui.screens.TeamStatsMainScreen
import com.frcteam1939.scouting2026.android.ui.screens.ViewTeamStatsScreen
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import com.frcteam1939.scouting2026.viewmodels.getViewModelFactory
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavigationHost(
    navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope
) {
    NavHost(navController, startDestination = AppRoutes.MatchScouting.GRAPH) {
        navigation(
            startDestination = AppRoutes.MatchScouting.SCOUT_MATCHES,
            route = AppRoutes.MatchScouting.GRAPH
        ) {
            composable(AppRoutes.MatchScouting.SCOUT_MATCHES) { backStackEntry ->
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
            composable(AppRoutes.PitScouting.SCOUT_PITS) { backStackEntry ->
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
            startDestination = AppRoutes.MatchReports.MATCH_REPORTS, route = AppRoutes.PitScouting.GRAPH
        ) {
            composable(AppRoutes.MatchReports.MATCH_REPORTS) {
                MainScreenScaffold(navController, drawerState, scope) {
                    // TODO: match report generation
                }
            }
        }
        navigation(
            startDestination = AppRoutes.TeamStats.TEAM_STATS, route = AppRoutes.TeamStats.GRAPH
        ) {
            composable(AppRoutes.TeamStats.TEAM_STATS) { backStackEntry ->
                val dataViewModel = getDataViewModel(backStackEntry, navController, AppRoutes.TeamStats.GRAPH)
                MainScreenScaffold(navController, drawerState, scope) {
                    TeamStatsMainScreen(dataViewModel, navController)
                }
            }
            composable(AppRoutes.TeamStats.VIEW_TEAM_STATS) { backStackEntry ->
                val dataViewModel = getDataViewModel(backStackEntry, navController, AppRoutes.TeamStats.GRAPH)
                ViewTeamStatsScreen(dataViewModel, navController)
            }
        }
        composable(AppRoutes.SYNC) { backStackEntry ->
            // TODO: device & data synchronization
            val scoutingViewModel = getScoutingViewModel(
                backStackEntry, navController, AppRoutes.SYNC
            )
            MainScreenScaffold(navController, drawerState, scope) {
                SyncScreen(scoutingViewModel, navController)
            }
        }
        navigation(startDestination = AppRoutes.EditForms.EDIT_FORMS, route = AppRoutes.EditForms.GRAPH) {
            composable(AppRoutes.EditForms.EDIT_FORMS) { backStackEntry ->
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
            composable(AppRoutes.Settings.SETTINGS) { backStackEntry ->
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
