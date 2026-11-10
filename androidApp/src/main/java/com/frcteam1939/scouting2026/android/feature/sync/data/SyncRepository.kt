package com.frcteam1939.scouting2026.android.feature.sync.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.BTClient
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.BTServer
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.BTUtils
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnectResult
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnectStatus
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnection
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTIOEvent
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTIOResult
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTIOStreamDataType
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerEvent
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerResult
import java.io.IOException
import java.util.HashMap
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SyncRepository(context: Context) {
    fun interface OnConnectionChangeListener {
        fun onChanged(connections: Map<String, BTConnection>)
    }

    interface OnIOListener {
        fun onBTInputEvent(btIOResult: BTIOResult)
        fun onBTOutputEvent(btIOEvent: BTIOEvent)
    }

    private val btClient: BTClient
    private val btServer: BTServer
    private val context: Context = context.applicationContext
    private val currentConnection: MutableMap<String, BTConnection> = ConcurrentHashMap()
    private val ioExecutor: ExecutorService = Executors.newCachedThreadPool()
    private var listener: OnConnectionChangeListener? = null
    private var ioListener: OnIOListener? = null

    init {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        btClient = BTClient(bluetoothAdapter)
        btServer = BTServer(bluetoothAdapter)
    }

    fun setOnConnectionChangeListener(listener: OnConnectionChangeListener?) {
        this.listener = listener
    }

    fun setOnIOListener(ioListener: OnIOListener?) {
        this.ioListener = ioListener
    }

    private fun notifyConnectionChanged() {
        if (listener != null) {
            listener!!.onChanged(HashMap(currentConnection))
        }
    }

    fun connectToDevice(device: BluetoothDevice?): CompletableFuture<BTConnectResult> {
        if (currentConnection.containsKey(device!!.address)) {
            return CompletableFuture.completedFuture(
                BTConnectResult(BTConnectStatus.SKIPPED_ALREADY_CONNECTED, null)
            )
        }

        return btClient.connectToDevice(device).thenApply { result ->
            if (result.status == BTConnectStatus.SUCCEEDED) {
                currentConnection[result.connection!!.remoteDeviceAddress] = result.connection
                notifyConnectionChanged()
                startIOListener(result.connection)
            }
            result
        }
    }

    fun startServerListening(): BTServerEvent {
        return btServer.startServerListening()
    }

    fun acceptRemoteConnection(): CompletableFuture<BTServerResult> {
        return btServer.acceptRemoteConnection().thenApply { result ->
            if (result.event == BTServerEvent.CONNECT_SUCCESS) {
                currentConnection[result.connection!!.remoteDeviceAddress] = result.connection
                notifyConnectionChanged()
                startIOListener(result.connection)
            }
            result
        }
    }

    fun closeBTServer(): BTServerEvent {
        val event = btServer.closeBTServer()
        if (event == BTServerEvent.OFF || event == BTServerEvent.OFF_SKIPPED) {
            closeAllConnections()
        }
        return event
    }

    fun closeExistingConnection(btConnection: BTConnection?) {
        val old = currentConnection.remove(btConnection!!.remoteDeviceAddress)
        if (old != null) {
            old.closeSocket()
            notifyConnectionChanged()
        }
    }

    private fun closeAllConnections() {
        for (btConnection in currentConnection.values) {
            btConnection.closeSocket()
        }
        currentConnection.clear()
        notifyConnectionChanged()
    }

    fun getPairedDevices(): List<BluetoothDevice> {
        return BTUtils.getPairedDevice(context)
    }

    fun shutdown() {
        closeAllConnections()
        btClient.shutdown()
        btServer.shutdown()
    }

    fun sendMsg(btConnection: BTConnection?, msg: String) {
        ioExecutor.submit {
            try {
                btConnection!!.sendMsg(msg)
                ioListener!!.onBTOutputEvent(BTIOEvent.SENT)
            } catch (e: IOException) {
                ioListener!!.onBTOutputEvent(BTIOEvent.SEND_FAILED)
            }
        }
    }

    fun startIOListener(btConnection: BTConnection) {
        ioExecutor.submit {
            while (btConnection.isConnected) {
                try {
                    val bytes = btConnection.receiveMsg()
                    ioListener!!.onBTInputEvent(BTIOResult(BTIOEvent.RECEIVED, BTIOStreamDataType.STRING, bytes))
                } catch (e: IOException) {
                    ioListener!!.onBTInputEvent(BTIOResult(BTIOEvent.RECEIVE_FAILED, null, null))
                    closeExistingConnection(btConnection)
                    break
                }
            }
        }
    }
}
