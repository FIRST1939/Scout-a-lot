package com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.util.Log
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnection
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerEvent
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerResult
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("MissingPermission")
class BTServer(private val bluetoothAdapter: BluetoothAdapter?) {
    private val bluetoothServerExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    @Volatile private var isListening = false
    @Volatile private var bluetoothServerSocket: BluetoothServerSocket? = null

    // Auto accept
    fun startServerListening(): BTServerEvent {
        if (isListening) {
            return BTServerEvent.ON_SKIPPED
        }
        isListening = true
        try {
            bluetoothServerSocket = bluetoothAdapter!!.listenUsingInsecureRfcommWithServiceRecord("Sync", BTUtils.BT_UUID)
            return BTServerEvent.ON
        } catch (e: IOException) {
            Log.e(TAG, "ERROR starting server", e)
            isListening = false
            return BTServerEvent.ON_FAILED
        }
    }

    fun acceptRemoteConnection(): CompletableFuture<BTServerResult> {
        return CompletableFuture.supplyAsync({
            try {
                val bluetoothSocket = bluetoothServerSocket!!.accept()
                BTServerResult(BTServerEvent.CONNECT_SUCCESS, BTConnection(bluetoothSocket))
            } catch (e: IOException) {
                if (isListening) {
                    Log.e(TAG, "Error while accepting connection", e)
                    BTServerResult(BTServerEvent.CONNECT_FAILED, null)
                } else {
                    BTServerResult(BTServerEvent.OFF, null)
                }
            }
        }, bluetoothServerExecutor)
    }

    fun closeBTServer(): BTServerEvent {
        if (!isListening) return BTServerEvent.OFF_SKIPPED
        isListening = false
        if (bluetoothServerSocket != null) {
            try {
                bluetoothServerSocket!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "Failed to close BluetoothServerSocket", e)
                return BTServerEvent.OFF_FAILED
            }
        }
        return BTServerEvent.OFF
    }

    fun shutdown() {
        closeBTServer()
        bluetoothServerExecutor.shutdownNow()
    }

    companion object {
        private const val TAG = "BTServer"
    }
}
