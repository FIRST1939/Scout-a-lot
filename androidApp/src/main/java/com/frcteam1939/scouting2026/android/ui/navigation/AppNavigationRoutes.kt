package com.frcteam1939.scouting2026.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object AppRoutes {
    // TODO: reformat names to not use spaces
    object MatchScouting {
        const val GRAPH = "match_scouting_graph"
        const val SCOUT_MATCHES = "scout matches"
        const val TEAM_CONFIRMATION = "team confirmation"
        const val SCOUT_AUTO = "scout auto"
        const val SCOUT_TELEOP = "scout teleop"
        const val SCOUT_ENDGAME = "scout endgame"
    }

    object PitScouting {
        const val GRAPH = "pit_scouting_graph"
        const val SCOUT_PITS = "scout pits"
        const val SCOUT_PITS_FORM = "scout pits form"
    }
    const val MATCH_REPORTS = "match reports"
    const val TEAM_STATS = "team info"
    const val SYNC = "sync"
    const val EDIT_FORMS = "edit forms"
    const val SELECT = "select"
    const val SAVED = "saved"

    object Settings {
        const val GRAPH = "settings_graph"
        const val SETTINGS = "settings"
        const val EDIT_EVENTS = "edit events"
        const val ADD_EVENT = "add event"
    }
    const val OPERATOR = "operator"
}

// Properties specific to the Navigation Rail
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object ScoutMatches :
        Screen(AppRoutes.MatchScouting.SCOUT_MATCHES, "Scout Matches", Icons.Default.Build)
    object ScoutPits : Screen(AppRoutes.PitScouting.SCOUT_PITS, "Scout Pits", Icons.Default.Person)
    object MatchReports : Screen(AppRoutes.MATCH_REPORTS, "Match Reports", Icons.Default.Info)
    object TeamStats : Screen(AppRoutes.TEAM_STATS, "Team Stats", Icons.Default.List)
    object Sync : Screen(AppRoutes.SYNC, "Sync", Icons.Default.Refresh)
    object EditForms : Screen(AppRoutes.EDIT_FORMS, "Edit Forms", Icons.Default.Edit)
    object Settings : Screen(AppRoutes.Settings.SETTINGS, "Settings", Icons.Default.Settings)
}