package com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnectResult
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnectStatus
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnection
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("MissingPermission")
class BTClient(private val bluetoothAdapter: BluetoothAdapter?) {
    private val bluetoothClientExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun connectToDevice(bluetoothDevice: BluetoothDevice): CompletableFuture<BTConnectResult> {
        return CompletableFuture.supplyAsync({
            var bluetoothSocket: BluetoothSocket? = null
            try {
                if (bluetoothAdapter == null) {
                    Log.e(TAG, "Bluetooth adapter is null")
                    return@supplyAsync BTConnectResult(BTConnectStatus.FAILED_NO_ADAPTER, null)
                }
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(BTUtils.BT_UUID)
                try {
                    bluetoothAdapter.cancelDiscovery()
                } catch (securityException: SecurityException) {
                    Log.w(TAG, "No BLUETOOTH_SCAN permission, skip cancelDiscovery()", securityException)
                }
                bluetoothSocket.connect()
                BTConnectResult(BTConnectStatus.SUCCEEDED, BTConnection(bluetoothSocket))
            } catch (e: SecurityException) {
                Log.e(TAG, "Permission Needed", e)
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close()
                    }
                } catch (closeException: IOException) {
                    Log.e(TAG, "Close Connection Failed", closeException)
                }
                BTConnectResult(BTConnectStatus.FAILED_NO_PERMISSION, null)
            } catch (e: IOException) {
                Log.e(TAG, "God damn Connection Failed, server on?", e)
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close()
                    }
                } catch (closeException: IOException) {
                    Log.e(TAG, "Close Connection Failed", closeException)
                }
                BTConnectResult(BTConnectStatus.FAILED_TO_CONNECT, null)
            }
        }, bluetoothClientExecutor)
    }

    fun shutdown() {
        bluetoothClientExecutor.shutdownNow()
    }

    companion object {
        private const val TAG = "BTClient"
    }
}
