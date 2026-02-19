package com.frcteam1939.scouting2026.android.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs

@Composable
fun SyncScreen(navController: NavController) {
    fun startActivity(context: Intent) {}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sync",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )
        UserInputs.TextButtonInput(
            onClick = {
                fun openBluetoothSettings() {
                    val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                    startActivity(intent)
                }
            },
            text = "Send"
        )
        TextButton(onClick = {}) { Text(text = "Recieve") }
    }
}