package com.frcteam1939.scouting2026.android.feature.sync;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnection;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTIOEvent;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTIOResult;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerEvent;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerResult;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerState;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SyncViewModel extends AndroidViewModel {
    private static final String PIT_TRANSFER_PREFIX = "BT_SYNC_PIT\n";
    private static final String MATCH_TRANSFER_PREFIX = "BT_SYNC_MATCH\n";

    private final SyncRepository repository;
    private final MutableLiveData<BTServerState> _btServerStatus = new MutableLiveData<>(BTServerState.OFF);
    private final MutableLiveData<Map<String, BTConnection>> _currentConnection = new MutableLiveData<>();
    private final MutableLiveData<List<BluetoothDevice>> _pairedDevices;
    private final MutableLiveData<String> _uiMessage = new MutableLiveData<>(null);
    private final MutableLiveData<IncomingBluetoothTransfer> _incomingTransfer = new MutableLiveData<>(null);

    public SyncViewModel(@NonNull Application application) {
        super(application);
        repository = new SyncRepository(application.getApplicationContext());
        _pairedDevices = new MutableLiveData<>(repository.getPairedDevices());
        repository.setOnConnectionChangeListener(_currentConnection::postValue);
        repository.setOnIOListener(new SyncRepository.OnIOListener() {
            @Override
            public void onBTInputEvent(BTIOResult btIOResult) {
                if (btIOResult.btioEvent() == BTIOEvent.RECEIVED) {
                    String msg = new String(btIOResult.bytes(), StandardCharsets.UTF_8);
                    IncomingBluetoothTransfer incomingTransfer = parseIncomingTransfer(msg);

                    if (incomingTransfer != null) {
                        _incomingTransfer.postValue(incomingTransfer);
                    } else {
                        _uiMessage.postValue("Received: " + msg);
                    }
                } else {
                    _uiMessage.postValue("Failed to receive message");
                }
            }

            @Override
            public void onBTOutputEvent(BTIOEvent btIOEvent) {
                if (btIOEvent == BTIOEvent.SENT) {
                    _uiMessage.postValue("Sent!");
                } else {
                    _uiMessage.postValue("Failed to send message");
                }
            }
        });
    }

    public LiveData<List<BluetoothDevice>> getPairedDevices() {
        return _pairedDevices;
    }

    public void refreshPairedDevices() {
        _pairedDevices.setValue(repository.getPairedDevices());
    }

    public LiveData<String> getUiMessage() {
        return _uiMessage;
    }

    public void clearUiMessage() {
        _uiMessage.setValue(null);
    }

    public LiveData<IncomingBluetoothTransfer> getIncomingTransfer() {
        return _incomingTransfer;
    }

    public void clearIncomingTransfer() {
        _incomingTransfer.setValue(null);
    }

    public LiveData<BTServerState> getBTServerStatus() {
        return _btServerStatus;
    }

    public LiveData<Map<String, BTConnection>> getCurrentConnection() {
        return _currentConnection;
    }

    public void closeExistingConnection(BTConnection btConnection) {
        repository.closeExistingConnection(btConnection);
        _uiMessage.setValue("Closing connection...");
    }

    public void connectToDevice(BluetoothDevice bluetoothDevice) {
        repository.connectToDevice(bluetoothDevice).thenAccept(result -> {
            switch (result.status()) {
                case SUCCEEDED -> {
                    _uiMessage.postValue(String.format("Successfully connected to %s",
                            result.connection().getRemoteDeviceName()));
                }
                case SKIPPED_ALREADY_CONNECTED ->
                        _uiMessage.postValue("Already connected to this device!");
                case FAILED_TO_CONNECT ->
                        _uiMessage.postValue("Failed to connect. Server on?");
                case FAILED_NO_ADAPTER ->
                        _uiMessage.postValue("No Bluetooth Adapter.");
                case FAILED_NO_PERMISSION ->
                        _uiMessage.postValue("No Bluetooth permission.");
                default ->
                        _uiMessage.postValue("Unknown error.");
            }
        });
    }

    public void startBTServerAutoAccept() {
        _btServerStatus.postValue(BTServerState.LISTENING);
        switch (repository.startServerListening()) {
            case ON -> {
                _uiMessage.setValue("BT server started, waiting for connection...");
                repository.acceptRemoteConnection().thenAccept(this::handleServerAcceptResult);
            }
            case ON_SKIPPED -> _uiMessage.postValue("Already listening! Skipped");
            case ON_FAILED -> {
                _uiMessage.postValue("Failed to start the server");
                _btServerStatus.postValue(BTServerState.OFF);
            }
        }
    }

    private void handleServerAcceptResult(BTServerResult result) {
        if (result.event() == BTServerEvent.CONNECT_SUCCESS) {
            _uiMessage.postValue(String.format("Successfully auto accept connection from %s",
                    result.connection().getRemoteDeviceName()));
            repository.acceptRemoteConnection().thenAccept(this::handleServerAcceptResult);
        } else if (result.event() == BTServerEvent.OFF) {
            return;
        } else {
            _uiMessage.postValue("Failed to accept connection");
        }
    }

    public void closeBTServer() {
        switch (repository.closeBTServer()) {
            case OFF -> {
                _uiMessage.setValue("Successfully closed BT server.");
                _btServerStatus.setValue(BTServerState.OFF);
            }
            case OFF_SKIPPED -> _uiMessage.setValue("Already closed");
            case OFF_FAILED -> _uiMessage.setValue("Failed to close BT server");
        }
    }

    public void sendMsg(BTConnection btConnection, String msg) {
        repository.sendMsg(btConnection,msg);
    }

    public void sendPitData(BTConnection btConnection, String exportedData) {
        sendTransfer(btConnection, IncomingBluetoothTransferType.PIT, exportedData);
    }

    public void sendMatchData(BTConnection btConnection, String exportedData) {
        sendTransfer(btConnection, IncomingBluetoothTransferType.MATCH, exportedData);
    }

    private void sendTransfer(BTConnection btConnection, IncomingBluetoothTransferType type, String exportedData) {
        if (btConnection == null) {
            _uiMessage.setValue("No connected device selected.");
            return;
        }

        String prefix = type == IncomingBluetoothTransferType.PIT
                ? PIT_TRANSFER_PREFIX
                : MATCH_TRANSFER_PREFIX;

        repository.sendMsg(btConnection, prefix + exportedData);
    }

    private IncomingBluetoothTransfer parseIncomingTransfer(String msg) {
        if (msg.startsWith(PIT_TRANSFER_PREFIX)) {
            return new IncomingBluetoothTransfer(
                    IncomingBluetoothTransferType.PIT,
                    msg.substring(PIT_TRANSFER_PREFIX.length())
            );
        }

        if (msg.startsWith(MATCH_TRANSFER_PREFIX)) {
            return new IncomingBluetoothTransfer(
                    IncomingBluetoothTransferType.MATCH,
                    msg.substring(MATCH_TRANSFER_PREFIX.length())
            );
        }

        return null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}
