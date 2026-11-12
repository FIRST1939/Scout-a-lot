package com.frcteam1939.scouting2026.android.feature.sync.ui

import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.frcteam1939.scouting2026.android.feature.sync.data.protocol.IncomingBluetoothTransferType
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnection
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerState
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.feature.sync.FileSyncViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.frcteam1939.scouting2026.android.ui.scoutColors
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen(
    syncViewModel: SyncViewModel,
    fileSyncViewModel: FileSyncViewModel,
    onOpenFileSync: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // BT Variables
    var selectedPairedDevice by remember { mutableStateOf<BluetoothDevice?>(null) }
    val uiMsg by syncViewModel.uiMessage.observeAsState(null)
    val incomingTransfer by syncViewModel.incomingTransfer.observeAsState(null)
    val pairedDevices by syncViewModel.pairedDevices.observeAsState(emptyList())
    val currentConnection by syncViewModel.currentConnection.observeAsState(emptyMap())
    val connectionList = currentConnection.values.toList()
    val btServerStatus by syncViewModel.btServerStatus.observeAsState(BTServerState.OFF)
    val checked = btServerStatus == BTServerState.LISTENING
    val isServerOn = btServerStatus == BTServerState.LISTENING


    LaunchedEffect(pairedDevices) {
        if (selectedPairedDevice != null &&
            pairedDevices.none { it.address == selectedPairedDevice?.address }) {
            selectedPairedDevice = null
        }
    }

    LaunchedEffect(uiMsg) {
        if (uiMsg != null) {
            Toast.makeText(context, uiMsg, Toast.LENGTH_SHORT).show()
            syncViewModel.clearUiMessage()
        }
    }

    LaunchedEffect(incomingTransfer) {
        val transfer = incomingTransfer ?: return@LaunchedEffect

        when (transfer.type) {
            IncomingBluetoothTransferType.PIT -> {
                if (transfer.content.isBlank()) {
                    Toast.makeText(context, "Received empty pit data", Toast.LENGTH_SHORT).show()
                } else {
                    fileSyncViewModel.importPitData(transfer.content)
                    Toast.makeText(context, "Pit data imported successfully", Toast.LENGTH_SHORT).show()
                }
            }
            IncomingBluetoothTransferType.MATCH -> {
                if (transfer.content.isBlank()) {
                    Toast.makeText(context, "Received empty match data", Toast.LENGTH_SHORT).show()
                } else {
                    fileSyncViewModel.importMatchData(transfer.content)
                    Toast.makeText(context, "Match data imported successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }

        syncViewModel.clearIncomingTransfer()
    }

    fun safeDeviceName(device: BluetoothDevice): String? {
        return try {
            device.name
        } catch (_: SecurityException) {
            null
        } catch (_: Exception) {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
            Text(
                text = "Sync",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Left
            )

        val checked = btServerStatus == BTServerState.LISTENING


        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) ,

        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable bluetooth")
                Switch(checked = checked,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            syncViewModel.startBTServerAutoAccept()
                        } else {
                            syncViewModel.closeBTServer()
                        }
                    }
                )
            }
        }

        OutlinedButton(
            onClick = onOpenFileSync,
            modifier = Modifier.padding(vertical = 4.dp),
            content = {
                Text("Import/export file")
                Icon(imageVector = Icons.Default.UploadFile, contentDescription = "")
            }

        )


        var showDialog by remember { mutableStateOf(false) }
        var text by remember { mutableStateOf("") }
        var targetTestConnection by remember { mutableStateOf<BTConnection?>(null) }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    targetTestConnection = null
                },
                title = { Text("Enter message") },
                text = {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Message") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        targetTestConnection?.let { connection ->
                            syncViewModel.sendMsg(connection, text)
                        }
                        targetTestConnection = null
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        targetTestConnection = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Text(
            text = "Paired Devices",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            textAlign = TextAlign.Start
        )

        if (pairedDevices.isNullOrEmpty()) {
            Text(
                text = "No paired Bluetooth devices found. Pair devices in your system settings",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(items = pairedDevices, key = { it.address }) { device ->
                    val isSelected = selectedPairedDevice?.address == device.address
                    Card(
                        onClick = { selectedPairedDevice = device },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Text(
                            text = safeDeviceName(device) ?: "Unknown Device",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = device.address,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {


            TextButton(
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        if (selectedPairedDevice == null) {
                            Toast.makeText(
                                context,
                                "Please select a device to connect first",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        if (!isServerOn) {
                            Toast.makeText(context, "Turn on the server here before connecting", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            syncViewModel.connectToDevice(selectedPairedDevice)
                        }
                    },
                    content = { Text("Connect") },

                    )
            OutlinedButton(
                onClick = {
                    syncViewModel.refreshPairedDevices()
                    Toast.makeText(context, "Device list refreshed", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text("Refresh Devices")
            }
        }

        Text(
            text = "Connected Devices",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            textAlign = TextAlign.Start
        )

        if (currentConnection.isNullOrEmpty()) {
            Text(
                text = "No devices are connected",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(items = connectionList, key = { it.remoteDeviceAddress }) { connection ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = connection.remoteDeviceName,
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = connection.remoteDeviceAddress,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row (
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 1.dp)
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 1.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                onClick = {
                                    coroutineScope.launch {
                                        val pitData = fileSyncViewModel.getPitDataForExport()
                                        if (pitData.isBlank()) {
                                            Toast.makeText(context, "No pit data to send", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            syncViewModel.sendPitData(connection, pitData)
                                        }
                                    }
                                },
                                content = {
                                    Text(
                                        "Send Pit Data",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                            )
                            TextButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 1.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                onClick = {
                                    coroutineScope.launch {
                                        val matchData = fileSyncViewModel.getMatchDataForExport()
                                        if (matchData.isBlank()) {
                                            Toast.makeText(context, "No match data to send", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            syncViewModel.sendMatchData(connection, matchData)
                                        }
                                    }
                                },
                                content = {
                                    Text(
                                        "Send Match Data",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 1.dp),
                        ) {
                            OutlinedButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 1.dp),
                                onClick = {
                                    targetTestConnection = connection
                                    showDialog = true
                                },
                                content = {
                                    Text(
                                        "Test Connection",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                            TextButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 1.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.scoutColors.redText,
                                ),
                                content = {
                                    Text(
                                        "Disconnect",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                onClick = {
                                    syncViewModel.closeExistingConnection(connection)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
