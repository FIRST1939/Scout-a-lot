package com.frcteam1939.scouting2026.android.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.frcteam1939.scouting2026.android.OperatorScreen
import com.frcteam1939.scouting2026.android.SavedScreen
import com.frcteam1939.scouting2026.android.ScoutingApp
import com.frcteam1939.scouting2026.android.SelectScreen

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ScoutingAppPreview() {
    PreviewSurface { ScoutingApp() }
}

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun ScoutMatchesScreenPreview() {
//    PreviewSurface { ScoutMatchesScreen(navController = rememberNavController()) }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun TeamConfirmationScreenPreview() {
//    PreviewSurface { TeamConfirmationScreen(navController = rememberNavController()) }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun SettingsScreenPreview() {
//    PreviewSurface { SettingsScreen(navController = rememberNavController()) }
//}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun OperatorScreenPreview() {
    PreviewSurface { OperatorScreen(navController = rememberNavController()) }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SavedScreenPreview() {
    PreviewSurface { SavedScreen(navController = rememberNavController()) }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SelectScreenPreview() {
    PreviewSurface { SelectScreen(navController = rememberNavController()) }
}

@Composable
private fun PreviewSurface(content: @Composable () -> Unit) {
    MyApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}
