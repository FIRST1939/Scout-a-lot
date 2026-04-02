package com.frcteam1939.scouting2026.android.feature.sync

import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnection
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerState
import com.frcteam1939.scouting2026.android.ui.UserInputs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen(
    syncViewModel: SyncViewModel,
    onOpenFileSync: () -> Unit,
) {
    val context = LocalContext.current
    // BT Variables
    var selectedPairedDevice by remember { mutableStateOf<BluetoothDevice?>(null) }
    var selectedConnectedDevice by remember { mutableStateOf<BTConnection?>(null) }
    val uiMsg by syncViewModel.uiMessage.observeAsState(null)
    val pairedDevices by syncViewModel.pairedDevices.observeAsState(emptyList())
    val currentConnection by syncViewModel.currentConnection.observeAsState(emptyMap())
    val connectionList = currentConnection.values.toList()
    val btServerStatus by syncViewModel.btServerStatus.observeAsState(BTServerState.OFF)

    LaunchedEffect(pairedDevices) {
        if (selectedPairedDevice != null &&
            pairedDevices.none { it.address == selectedPairedDevice?.address }) {
            selectedPairedDevice = null
        }
    }

    LaunchedEffect(currentConnection) {
        if (selectedConnectedDevice != null &&
            connectionList.none { it.remoteDeviceAddress == selectedConnectedDevice?.remoteDeviceAddress }) {
            selectedConnectedDevice = null
        }
    }

    LaunchedEffect(uiMsg) {
        if (uiMsg != null) {
            Toast.makeText(context, uiMsg, Toast.LENGTH_SHORT).show()
            syncViewModel.clearUiMessage()
        }
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sync",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        UserInputs.TextButtonInput(
            onClick = onOpenFileSync,
            text = "Open File Sync",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        val checked = btServerStatus == BTServerState.LISTENING

        UserInputs.SwitchTextBarInput(
            checked = checked,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    syncViewModel.startBTServerAutoAccept()
                } else {
                    syncViewModel.closeBTServer()
                }
            },
            text = "BT Server"
        )


        UserInputs.TextButtonInput(
            onClick = {
                if (selectedPairedDevice == null) {
                    Toast.makeText(context, "Please select a device to connect first", Toast.LENGTH_SHORT).show()
                } else {
                    syncViewModel.connectToDevice(selectedPairedDevice)
                }
            },
            text = "Connect",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                if (selectedConnectedDevice == null) {
                    Toast.makeText(context, "Please select a device to disconnect first", Toast.LENGTH_SHORT).show()
                } else {
                    syncViewModel.closeExistingConnection(selectedConnectedDevice)
                }
            },
            text = "Disconnect",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                // TODO()
            },
            text = "Test Connection",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                syncViewModel.refreshPairedDevices()
                Toast.makeText(context, "Device list refreshed", Toast.LENGTH_SHORT).show()
            },
            text = "Refresh Devices",
            modifier = Modifier.padding(vertical = 4.dp)
        )

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
                text = "No paired Bluetooth devices found",
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
                text = "No connected Bluetooth devices found",
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
                items(items = connectionList, key = { it.remoteDeviceAddress }) { connection ->
                    val isSelected = selectedConnectedDevice?.remoteDeviceAddress == connection.remoteDeviceAddress
                    Card(
                        onClick = { selectedConnectedDevice = connection },
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
                            text = connection.remoteDeviceName,
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = connection.remoteDeviceAddress,
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
    }
}
