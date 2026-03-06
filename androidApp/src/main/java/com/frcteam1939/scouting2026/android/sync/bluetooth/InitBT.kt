package com.frcteam1939.scouting2026.android.sync.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

private fun openBluetoothSettingsSafely(context: Context): Boolean {
    val intents = buildList {
        // Standard Android Bluetooth settings entry.
        add(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))

        // Explicit settings components that often exist on MIUI/AOSP builds.
        add(
            Intent().setComponent(
                ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings\$BluetoothSettingsActivity"
                )
            )
        )
        add(
            Intent().setClassName(
                "com.android.settings",
                "com.android.settings.bluetooth.BluetoothSettings"
            )
        )

        // Final fallbacks.
        add(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        add(Intent(Settings.ACTION_SETTINGS))
    }

    intents.forEach { baseIntent ->
        val intent = Intent(baseIntent)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return true
            }
        } catch (_: Exception) {
            // Try next fallback intent.
        }
    }

    return false
}

@Composable
fun InitBT() {
    val context = LocalContext.current
    val bluetoothManager: BluetoothManager? =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    fun showBluetoothStatusToast() {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth unavailable on this device", Toast.LENGTH_SHORT).show()
            return
        }

        val isEnabled = try {
            bluetoothAdapter.isEnabled
        } catch (_: SecurityException) {
            null
        } catch (_: Exception) {
            null
        }

        when (isEnabled) {
            true -> Toast.makeText(context, "Bluetooth is on", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Bluetooth is off", Toast.LENGTH_SHORT).show()
            null -> Toast.makeText(context, "Could not read Bluetooth state", Toast.LENGTH_SHORT).show()
        }
    }

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

    fun launchEnableBluetoothFlow() {
        if (bluetoothAdapter == null) return
        if (bluetoothAdapter.isEnabled) {
            return
        }

        // Prefer system dialog first so user can explicitly confirm enabling Bluetooth.
        try {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
            return
        } catch (_: ActivityNotFoundException) {
            // Continue to fallback below.
        } catch (_: Exception) {
            // Continue to fallback below.
        }

        // MIUI/older ROM fallback: direct enable can still work when request dialog is unavailable.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            try {
                if (bluetoothAdapter.enable()) {
                    Toast.makeText(context, "Turning on Bluetooth...", Toast.LENGTH_SHORT).show()
                    return
                }
            } catch (_: Exception) {
                // Continue to settings fallback.
            }
        }

        if (!openBluetoothSettingsSafely(context)) {
            Toast.makeText(context, "Could not open Bluetooth settings", Toast.LENGTH_SHORT).show()
        }
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = permissions[android.Manifest.permission.BLUETOOTH_CONNECT] ?: false
            if (isGranted) {
                showBluetoothStatusToast()
                // Now that we have permission, we can try to enable Bluetooth
                launchEnableBluetoothFlow()
            } else {
                Toast.makeText(context, "Bluetooth permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    LaunchedEffect(Unit) {
        if (bluetoothAdapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12+ (API 31+): Must request BLUETOOTH_CONNECT via dialog
                permissionLauncher.launch(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT))
            } else {
                showBluetoothStatusToast()
                // Android 11 and lower: Permissions are granted automatically if in Manifest.
                // Just check if BT is on, and if not, show the "Enable Bluetooth" system dialog.
                try {
                    if (!bluetoothAdapter.isEnabled) {
                        launchEnableBluetoothFlow()
                    }
                } catch (e: Exception) {
                    // Safety check for devices with restricted profiles
                    if (!openBluetoothSettingsSafely(context)) {
                        Toast.makeText(context, "Could not open Bluetooth settings", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        } else {
            showBluetoothStatusToast()
        }
    }
}
