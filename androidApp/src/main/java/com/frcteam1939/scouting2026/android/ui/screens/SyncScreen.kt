package com.frcteam1939.scouting2026.android.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTServerStatus
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.viewmodels.BluetoothViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen (bluetoothViewModel: BluetoothViewModel) {
    val context = LocalContext.current
    // BT Variables
    var selectedDevice by remember { mutableStateOf<BluetoothDevice?>(null) }
    var fileContent by remember { mutableStateOf("") }
    val pairedDevices by bluetoothViewModel.pairedDevices.observeAsState(emptyList())
    val deviceName by bluetoothViewModel.connectedDevice.observeAsState()
    val btServerStatus by bluetoothViewModel.btServerStatus.observeAsState(BTServerStatus.OFF)

    LaunchedEffect(pairedDevices) {
        if (selectedDevice != null &&
            pairedDevices.none { it.address == selectedDevice?.address }) {
            selectedDevice = null
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sync",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                bluetoothViewModel.startBTServer()
            },
            text = "Start BT Server",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                bluetoothViewModel.closeBTServer()
            },
            text = "Kill BT Server",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        UserInputs.TextButtonInput(
            onClick = {
                if (selectedDevice == null) {
                    Toast.makeText(context, "Please select a device first", Toast.LENGTH_SHORT).show()
                } else {
                    bluetoothViewModel.connectToDevice(selectedDevice)
                }
            },
            text = "Connect",
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
                bluetoothViewModel.refreshPairedDevices()
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
                items(items = pairedDevices.orEmpty(), key = { it.address }) { device ->
                    val isSelected = selectedDevice?.address == device.address
                    Card(
                        onClick = { selectedDevice = device },
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

        val selectedDeviceText = selectedDevice?.let { device ->
            safeDeviceName(device) ?: device.address
        } ?: "None"
        Text(
            text = "Selected: $selectedDeviceText",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Start
        )

        Text(
            text = "Connected: $deviceName",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Start
        )

        Text(
            text = "Bluetooth Server Status: ${btServerStatus.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Start
        )

        if (fileContent.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = fileContent,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
