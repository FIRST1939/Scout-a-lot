package com.frcteam1939.scouting2026.android.feature.sync

// TODO: Merge FileSyncScreen into the Bluetooth Sync page after the unified sync UX is designed.

import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.storage.exportTextFile
import com.frcteam1939.scouting2026.viewmodels.FileSyncViewModel
import kotlinx.coroutines.launch

@Composable
fun FileSyncScreen(fileSyncViewModel: FileSyncViewModel) {
    val context = LocalContext.current
    var pitFileContent by remember { mutableStateOf("") }
    var matchFileContent by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val pitFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    pitFileContent = inputStream.bufferedReader().use { reader -> reader.readText() }
                }
                coroutineScope.launch {
                    fileSyncViewModel.importPitData(pitFileContent)
                    makeText(context, "Pit data imported successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                makeText(context, "Failed to read pit data file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val matchFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    matchFileContent = inputStream.bufferedReader().use { reader -> reader.readText() }
                }
                coroutineScope.launch {
                    fileSyncViewModel.importMatchData(matchFileContent)
                    makeText(context, "Match data imported successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                makeText(context, "Failed to read match data file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "File Sync",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "Pit Data",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                pitFilePickerLauncher.launch(arrayOf("text/plain"))
            },
            text = "Import Pit Data"
        )

        var pitFileName by remember { mutableStateOf("PitData.txt") }
        UserInputs.TextboxInput(
            value = pitFileName,
            onValueChange = { pitFileName = it },
            label = "Pit File Name:"
        )
        UserInputs.TextButtonInput(
            onClick = {
                coroutineScope.launch {
                    exportTextFile(
                        fileName = pitFileName,
                        content = fileSyncViewModel.getPitDataForExport(),
                    )
                    makeText(context, "Pit data exported", Toast.LENGTH_SHORT).show()
                }
            },
            text = "Export Pit Data"
        )

        Text(
            text = "Match Data",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                matchFilePickerLauncher.launch(arrayOf("text/plain"))
            },
            text = "Import Match Data"
        )

        var matchFileName by remember { mutableStateOf("MatchData.txt") }
        UserInputs.TextboxInput(
            value = matchFileName,
            onValueChange = { matchFileName = it },
            label = "Match File Name:"
        )
        UserInputs.TextButtonInput(
            onClick = {
                coroutineScope.launch {
                    exportTextFile(
                        fileName = matchFileName,
                        content = fileSyncViewModel.getMatchDataForExport(),
                    )
                    makeText(context, "Match data exported", Toast.LENGTH_SHORT).show()
                }
            },
            text = "Export Match Data"
        )

        if (pitFileContent.isNotEmpty()) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Pit Data:\n$pitFileContent",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (matchFileContent.isNotEmpty()) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Match Data:\n$matchFileContent",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
