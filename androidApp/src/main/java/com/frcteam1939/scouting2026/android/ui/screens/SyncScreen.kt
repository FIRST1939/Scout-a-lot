package com.frcteam1939.scouting2026.android.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
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
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.storage.exportTextFile
import com.frcteam1939.scouting2026.storage.readTextFile

const val PICK_PDF_FILE = 2

fun openFile(pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
    }

    startActivityForResult(intent, PICK_PDF_FILE)
}


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
        TextButton(
            onClick = {}
        ) {
            Text(text = "Import")
        }
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
