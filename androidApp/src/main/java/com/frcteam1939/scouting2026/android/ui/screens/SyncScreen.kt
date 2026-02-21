package com.frcteam1939.scouting2026.android.ui.screens

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.storage.exportTextFile
import com.frcteam1939.scouting2026.storage.readTextFile

@Composable
fun SyncScreen(navController: NavController) {
    var fileContent by remember { mutableStateOf("") } // State to store the read text
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
        TextButton(
            onClick = {}
        ) {
            Text(text = "Recieve")
        }
        val context = LocalContext.current
        TextButton(
            onClick = {
                exportTextFile(
                    fileName = "bogosbinter.txt",
                    content = "Did you get the photos printed? Bogos binted? Bogos BINTed? BINT BINT BINT BRAK  DOMER"
                )
                makeText(context, "done✅done✅done✅done⚠️⚠️", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text(text = "Export")
        }
        TextButton(
            onClick = {
                val content = readTextFile("bogosbinter.txt")
                fileContent = content ?: "File not found or empty"
            }
        ) {
            Text(text = "Show Data")
        }

        if (fileContent.isNotEmpty()) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = fileContent,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}