package com.frcteam1939.scouting2026.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object AppRoutes {
    var titles = mapOf(
        MatchScouting.SCOUT_MATCHES to "Scout Matches",
        MatchScouting.TEAM_CONFIRMATION to "Team Confirmation",
        MatchScouting.SCOUT_AUTO to "Scout Auto",
        MatchScouting.SCOUT_TELEOP to "Scout Teleop",
        MatchScouting.SCOUT_ENDGAME to "Scout Endgame",
        PitScouting.SCOUT_PITS to "Scout Pits",
        PitScouting.SCOUT_PITS_FORM to "Scout Pit Form",
        MatchReports.MATCH_REPORTS to "Match Reports",
        MatchReports.VIEW_MATCH_REPORT to "View Match Report",
        TeamStats.TEAM_STATS to "Team Stats",
        TeamStats.VIEW_TEAM_STATS to "View Team Stats",
        SYNC to "Sync",
        DATA_CHECKLIST to "Data Checklist",
        EditForms.EDIT_FORMS to "Edit Forms",
        EditForms.EDIT_PIT_FORM to "Edit Pit Form",
        EditForms.EDIT_MATCH_FORM to "Edit Match Form",
        EditForms.ASSIGN_FORMS to "Assign Forms to Events",
        Settings.SELECT to "Select",
        Settings.SAVED to "Saved",
        Settings.SETTINGS to "Settings",
        Settings.EDIT_EVENTS to "Edit Events",
        Settings.ADD_EVENT to "Add Event",
        Settings.OPERATOR to "Operator",
    )
    object MatchScouting {
        const val GRAPH = "match_scouting_graph"
        const val SCOUT_MATCHES = "scout_matches"
        const val TEAM_CONFIRMATION = "team_confirmation"
        const val SCOUT_AUTO = "scout_auto"
        const val SCOUT_TELEOP = "scout_teleop"
        const val SCOUT_ENDGAME = "scout_endgame"
    }

    object PitScouting {
        const val GRAPH = "pit_scouting_graph"
        const val SCOUT_PITS = "scout_pits"
        const val SCOUT_PITS_FORM = "scout_pits_form"
    }

    object MatchReports {
        const val GRAPH = "match_reports_graph"
        const val MATCH_REPORTS = "match_reports"
        const val VIEW_MATCH_REPORT = "view_match_report"
    }

    object TeamStats {
        const val GRAPH = "team_stats_graph"
        const val TEAM_STATS = "team_stats"
        const val VIEW_TEAM_STATS = "view_team_stats"
    }

    const val SYNC = "sync"

    const val DATA_CHECKLIST = "data_checklist"

    object EditForms {
        const val GRAPH = "edit_forms_graph"
        const val EDIT_FORMS = "edit_forms"
        const val EDIT_PIT_FORM = "create_pit_form"
        const val EDIT_MATCH_FORM = "create_match_form"
        const val ASSIGN_FORMS = "assign_forms"
    }

    object Settings {
        const val GRAPH = "settings_graph"
        const val SETTINGS = "settings"
        const val EDIT_EVENTS = "edit_events"
        const val ADD_EVENT = "add_event"
        const val SELECT = "select"
        const val SAVED = "saved"
        const val OPERATOR = "operator"
    }

}

// Properties specific to the Navigation Rail
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Scouting : Screen(AppRoutes.MatchScouting.SCOUT_MATCHES, "Scouting", Icons.Default.Build)
    object ScoutMatches : Screen(AppRoutes.MatchScouting.SCOUT_MATCHES, "Scout Matches", Icons.Default.Build)
    object ScoutPits : Screen(AppRoutes.PitScouting.SCOUT_PITS, "Scout Pits", Icons.Default.Person)
    object MatchReports : Screen(AppRoutes.MatchReports.MATCH_REPORTS, "Match Reports", Icons.Default.Info)
    object TeamStats : Screen(AppRoutes.TeamStats.TEAM_STATS, "Team Stats", Icons.Default.List)
    object Sync : Screen(AppRoutes.SYNC, "Sync", Icons.Default.Refresh)
    object DataChecklist : Screen(AppRoutes.DATA_CHECKLIST, "Data Checklist", Icons.Default.Checklist)
    object EditForms : Screen(AppRoutes.EditForms.EDIT_FORMS, "Edit Forms", Icons.Default.Edit)
    object Settings : Screen(AppRoutes.Settings.SETTINGS, "Settings", Icons.Default.Settings)
}