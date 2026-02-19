import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.frcteam1939.scouting2026.android.getScoutingViewModel
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.navigation.MainScreenScaffold
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
import com.frcteam1939.scouting2026.viewmodels.SettingsViewModel
import com.frcteam1939.scouting2026.viewmodels.getViewModelFactory
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavigationHost(
    navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope
) {
    NavHost(navController = navController, startDestination = AppRoutes.MatchScouting.GRAPH) {
        navigation(
            startDestination = AppRoutes.MatchScouting.SCOUT_MATCHES,
            route = AppRoutes.MatchScouting.GRAPH
        ) {
            composable(AppRoutes.MatchScouting.SCOUT_MATCHES) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MainScreenScaffold(navController, drawerState, scope) {
                    ScoutMatchesScreen(
                        navController = navController, scoutingViewModel = scoutingViewModel
                    )
                }
            }
            composable(AppRoutes.MatchScouting.TEAM_CONFIRMATION) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                TeamConfirmationScreen(
                    navController = navController, scoutingViewModel = scoutingViewModel
                )
            }
            composable(AppRoutes.MatchScouting.SCOUT_AUTO) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MatchScoutingScaffold(navController) { _ ->
                    ScoutAutoScreen(scoutingViewModel = scoutingViewModel)
                }
            }
            composable(AppRoutes.MatchScouting.SCOUT_TELEOP) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MatchScoutingScaffold(navController) { _ ->
                    ScoutTeleopScreen(scoutingViewModel = scoutingViewModel)
                }
            }
            composable(AppRoutes.MatchScouting.SCOUT_ENDGAME) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.MatchScouting.GRAPH
                )
                MatchScoutingScaffold(navController) { _ ->
                    ScoutEndgameScreen(
                        navController = navController, scoutingViewModel = scoutingViewModel
                    )
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
                    ScoutPitScreen(scoutingViewModel = scoutingViewModel)
                }
            }
            composable(AppRoutes.PitScouting.SCOUT_PITS_FORM) { backStackEntry ->
                val scoutingViewModel = getScoutingViewModel(
                    backStackEntry, navController, AppRoutes.PitScouting.GRAPH
                )
                ScoutPitForm(scoutingViewModel = scoutingViewModel)
            }
        }
        composable(AppRoutes.MATCH_REPORTS) {
            MainScreenScaffold(navController, drawerState, scope) {
                // TODO: match report generation
            }
        }
        composable(AppRoutes.TEAM_STATS) {
            MainScreenScaffold(navController, drawerState, scope) {
                // TODO: team stats page (all teams)
            }
        }
        composable(AppRoutes.SYNC) {
            // TODO: device & data synchronization
            MainScreenScaffold(navController, drawerState, scope) {
                SyncScreen(navController = navController)
            }
        }
        composable(AppRoutes.EDIT_FORMS) {
            MainScreenScaffold(navController, drawerState, scope) {
                // TODO: add edit forms function
                // this is where saved and select will be
            }
        }
        navigation(
            startDestination = AppRoutes.Settings.SETTINGS, route = AppRoutes.Settings.GRAPH
        ) {
            composable(AppRoutes.Settings.SETTINGS) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoutes.Settings.GRAPH)
                }
                val settingsViewModel: SettingsViewModel = viewModel(
                    viewModelStoreOwner = parentEntry, factory = getViewModelFactory()
                )
                MainScreenScaffold(navController, drawerState, scope) {
                    SettingsScreen(
                        navController = navController, settingsViewModel = settingsViewModel
                    )
                }
            }
            composable(AppRoutes.Settings.EDIT_EVENTS) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoutes.Settings.GRAPH)
                }
                val settingsViewModel: SettingsViewModel = viewModel(
                    viewModelStoreOwner = parentEntry, factory = getViewModelFactory()
                )
                EditEventsScreen(
                    navController = navController, settingsViewModel = settingsViewModel
                )
            }
            composable(AppRoutes.Settings.ADD_EVENT) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoutes.Settings.GRAPH)
                }
                val settingsViewModel: SettingsViewModel = viewModel(
                    viewModelStoreOwner = parentEntry, factory = getViewModelFactory()
                )
                AddEventScreen(
                    navController = navController, settingsViewModel = settingsViewModel
                )
            }
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