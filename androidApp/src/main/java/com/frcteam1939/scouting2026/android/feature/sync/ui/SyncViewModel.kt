package com.frcteam1939.scouting2026.android.feature.sync.ui

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frcteam1939.scouting2026.android.feature.sync.data.SyncRepository
import com.frcteam1939.scouting2026.android.feature.sync.data.protocol.IncomingBluetoothTransfer
import com.frcteam1939.scouting2026.android.feature.sync.data.protocol.IncomingBluetoothTransferType
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnection
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTConnectStatus
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTIOEvent
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTIOResult
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerEvent
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerResult
import com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models.BTServerState
import java.nio.charset.StandardCharsets

class SyncViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SyncRepository
    private val _btServerStatus = MutableLiveData(BTServerState.OFF)
    private val _currentConnection = MutableLiveData<Map<String, BTConnection>>()
    private val _pairedDevices: MutableLiveData<List<BluetoothDevice>>
    private val _uiMessage = MutableLiveData<String?>(null)
    private val _incomingTransfer = MutableLiveData<IncomingBluetoothTransfer?>(null)

    init {
        repository = SyncRepository(application.applicationContext)
        _pairedDevices = MutableLiveData(repository.getPairedDevices())
        repository.setOnConnectionChangeListener(_currentConnection::postValue)
        repository.setOnIOListener(object : SyncRepository.OnIOListener {
            override fun onBTInputEvent(btIOResult: BTIOResult) {
                if (btIOResult.btioEvent == BTIOEvent.RECEIVED) {
                    val msg = String(btIOResult.bytes!!, StandardCharsets.UTF_8)
                    val incomingTransfer = parseIncomingTransfer(msg)

                    if (incomingTransfer != null) {
                        _incomingTransfer.postValue(incomingTransfer)
                    } else {
                        _uiMessage.postValue("Received a message: " + msg)
                    }
                } else {
                    _uiMessage.postValue("Disconnected")
                }
            }

            override fun onBTOutputEvent(btIOEvent: BTIOEvent) {
                if (btIOEvent == BTIOEvent.SENT) {
                    _uiMessage.postValue("Sent!")
                } else {
                    _uiMessage.postValue("Failed to send message")
                }
            }
        })
    }

    val pairedDevices: LiveData<List<BluetoothDevice>>
        get() = _pairedDevices

    fun refreshPairedDevices() {
        _pairedDevices.value = repository.getPairedDevices()
    }

    val uiMessage: LiveData<String?>
        get() = _uiMessage

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    val incomingTransfer: LiveData<IncomingBluetoothTransfer?>
        get() = _incomingTransfer

    fun clearIncomingTransfer() {
        _incomingTransfer.value = null
    }

    val btServerStatus: LiveData<BTServerState>
        get() = _btServerStatus

    val currentConnection: LiveData<Map<String, BTConnection>>
        get() = _currentConnection

    fun closeExistingConnection(btConnection: BTConnection?) {
        repository.closeExistingConnection(btConnection)
        _uiMessage.value = "Closing connection..."
    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice?) {
        repository.connectToDevice(bluetoothDevice).thenAccept { result ->
            when (result.status) {
                BTConnectStatus.SUCCEEDED -> {
                    _uiMessage.postValue(String.format("Successfully connected to %s", result.connection!!.remoteDeviceName))
                }
                BTConnectStatus.SKIPPED_ALREADY_CONNECTED ->
                    _uiMessage.postValue("Already connected to this device")
                BTConnectStatus.FAILED_TO_CONNECT ->
                    _uiMessage.postValue("Failed to connect. Is the other device's server on?")
                BTConnectStatus.FAILED_NO_ADAPTER ->
                    _uiMessage.postValue("Target device does not support Bluetooth")
                BTConnectStatus.FAILED_NO_PERMISSION ->
                    _uiMessage.postValue("The app does not have permission to use Bluetooth")
                else ->
                    _uiMessage.postValue("Unknown error.")
            }
        }
    }

    fun startBTServerAutoAccept() {
        _btServerStatus.postValue(BTServerState.LISTENING)
        when (repository.startServerListening()) {
            BTServerEvent.ON -> {
                _uiMessage.value = "Bluetooth server started, waiting for connection..."
                repository.acceptRemoteConnection().thenAccept(::handleServerAcceptResult)
            }
            BTServerEvent.ON_SKIPPED -> _uiMessage.postValue("Already listening, skipped")
            BTServerEvent.ON_FAILED -> {
                _uiMessage.postValue("Failed to start Bluetooth server")
                _btServerStatus.postValue(BTServerState.OFF)
            }
            else -> Unit
        }
    }

    private fun handleServerAcceptResult(result: BTServerResult) {
        if (result.event == BTServerEvent.CONNECT_SUCCESS) {
            _uiMessage.postValue(String.format("Auto accepted connection request from %s", result.connection!!.remoteDeviceName))
            repository.acceptRemoteConnection().thenAccept(::handleServerAcceptResult)
        } else if (result.event == BTServerEvent.OFF) {
            return
        } else {
            _uiMessage.postValue("Failed to accept a connection request")
        }
    }

    fun closeBTServer() {
        when (repository.closeBTServer()) {
            BTServerEvent.OFF -> {
                _uiMessage.value = "Successfully closed Bluetooth server."
                _btServerStatus.value = BTServerState.OFF
            }
            BTServerEvent.OFF_SKIPPED -> _uiMessage.value = "Already closed"
            BTServerEvent.OFF_FAILED -> _uiMessage.value = "Failed to close Bluetooth server"
            else -> Unit
        }
    }

    fun sendMsg(btConnection: BTConnection?, msg: String) {
        repository.sendMsg(btConnection, msg)
    }

    fun sendPitData(btConnection: BTConnection?, exportedData: String) {
        sendTransfer(btConnection, IncomingBluetoothTransferType.PIT, exportedData)
    }

    fun sendMatchData(btConnection: BTConnection?, exportedData: String) {
        sendTransfer(btConnection, IncomingBluetoothTransferType.MATCH, exportedData)
    }

    private fun sendTransfer(btConnection: BTConnection?, type: IncomingBluetoothTransferType, exportedData: String) {
        if (btConnection == null) {
            _uiMessage.value = "No connected device selected."
            return
        }

        val prefix = if (type == IncomingBluetoothTransferType.PIT) PIT_TRANSFER_PREFIX else MATCH_TRANSFER_PREFIX
        repository.sendMsg(btConnection, prefix + exportedData)
    }

    private fun parseIncomingTransfer(msg: String): IncomingBluetoothTransfer? {
        if (msg.startsWith(PIT_TRANSFER_PREFIX)) {
            return IncomingBluetoothTransfer(
                IncomingBluetoothTransferType.PIT,
                msg.substring(PIT_TRANSFER_PREFIX.length)
            )
        }

        if (msg.startsWith(MATCH_TRANSFER_PREFIX)) {
            return IncomingBluetoothTransfer(
                IncomingBluetoothTransferType.MATCH,
                msg.substring(MATCH_TRANSFER_PREFIX.length)
            )
        }

        return null
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdown()
    }

    companion object {
        private const val PIT_TRANSFER_PREFIX = "BT_SYNC_PIT\n"
        private const val MATCH_TRANSFER_PREFIX = "BT_SYNC_MATCH\n"
    }
}
