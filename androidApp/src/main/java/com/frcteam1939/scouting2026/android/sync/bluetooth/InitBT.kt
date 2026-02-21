package com.frcteam1939.scouting2026.android.sync.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun InitBT() {
    val context = LocalContext.current
    // Create a launcher to handle the Bluetooth enable request
    val enableBluetoothLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // Logic for when the user clicks 'Allow' or 'Deny' on the system Bluetooth popup
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Bluetooth is running..", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Bluetooth is off. Sync may not work..",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    val bluetoothManager: BluetoothManager? =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = permissions[android.Manifest.permission.BLUETOOTH_CONNECT] ?: false
            if (isGranted) {
                // Now that we have permission, we can try to enable Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothLauncher.launch(enableBtIntent)
            } else {
                Toast.makeText(context, "Bluetooth permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    LaunchedEffect(Unit) {
        if (bluetoothAdapter != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Android 12+ (API 31+): Must request BLUETOOTH_CONNECT via dialog
                permissionLauncher.launch(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT))
            } else {
                // Android 11 and lower: Permissions are granted automatically if in Manifest.
                // Just check if BT is on, and if not, show the "Enable Bluetooth" system dialog.
                try {
                    if (!bluetoothAdapter.isEnabled) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        enableBluetoothLauncher.launch(enableBtIntent)
                    }
                } catch (e: Exception) {
                    // Safety check for devices with restricted profiles
                    Toast.makeText(context, "Could not open Bluetooth settings", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}