package com.frcteam1939.scouting2026.android.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.storage.exportTextFile
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import kotlinx.coroutines.launch

@Composable
fun SyncScreen(scoutingViewModel: ScoutingViewModel, navController: NavController) {
    val context = LocalContext.current
    var fileContent by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Result launcher for picking a text file
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    fileContent = inputStream.bufferedReader().use { reader -> reader.readText() }
                }
                coroutineScope.launch {
                    scoutingViewModel.importPitData(fileContent)
                    makeText(context, "Data imported successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                makeText(context, "Failed to read file", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                context.startActivity(intent)
            },
            text = "Send"
        )
        UserInputs.TextButtonInput(
            onClick = {},
            text = "Receive"
        )

        UserInputs.TextButtonInput(
            onClick = {
                filePickerLauncher.launch(arrayOf("text/plain"))
            },
            text = "Import and Show Data"
        )

        var fileName by remember { mutableStateOf("ScoutedData.txt") }
        UserInputs.TextboxInput(
            value = fileName,
            onValueChange = { fileName = it },
            label = "File Name:"
        )
        UserInputs.TextButtonInput(
            onClick = {
                coroutineScope.launch {
                    exportTextFile(
                        fileName = fileName,
                        content = scoutingViewModel.getPitDataForExport(),
                    )
                    makeText(context, "Exported ✅", Toast.LENGTH_SHORT).show()
                }
            },
            text = "Export"
        )


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