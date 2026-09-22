package com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.Collections
import java.util.UUID

object BTUtils {
    @JvmField
    val BT_UUID: UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-0800200c9a66")

    @JvmStatic
    fun getPairedDevice(context: Context): List<BluetoothDevice> {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        if (bluetoothManager == null) {
            return Collections.emptyList()
        }
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            return Collections.emptyList()
        }

        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        }

        if (!hasPermission) return Collections.emptyList()

        try {
            val pairedDevicesList = java.util.List.copyOf(bluetoothAdapter.bondedDevices)
            return pairedDevicesList
        } catch (e: Exception) {
            return Collections.emptyList()
        }
    }
}
