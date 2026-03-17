package com.frcteam1939.scouting2026.viewmodels;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTClient;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTConnection;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTServer;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTServerEvent;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTServerState;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTUtils;
import java.util.List;

public class BluetoothViewModel extends AndroidViewModel {
    private final BTClient btClient;
    private final BTServer btServer;
    private final MutableLiveData<BTConnection> _btConnection;
    private final MutableLiveData<String> _btConnectionDeviceName = new MutableLiveData<>("");
    private final MutableLiveData<BTServerState> _btServerStatus = new MutableLiveData<>(BTServerState.OFF);
    private final MutableLiveData<List<BluetoothDevice>> _pairedDevices;
    private final MutableLiveData<String> _uiMessage = new MutableLiveData<>(null);

    public BluetoothViewModel(@NonNull Application application) {
        super(application);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btClient = new BTClient(bluetoothAdapter);
        btServer = new BTServer(bluetoothAdapter);
        _btConnection = new MutableLiveData<>(null);
        _pairedDevices = new MutableLiveData<>(BTUtils.getPairedDevice(application.getApplicationContext()));
    }

    public LiveData<List<BluetoothDevice>> getPairedDevices() {
        return _pairedDevices;
    }

    public void refreshPairedDevices() {
        _pairedDevices.setValue(BTUtils.getPairedDevice(getApplication().getApplicationContext()));
    }

    public LiveData<String> getUiMessage() {
        return _uiMessage;
    }

    public void clearUiMessage() {
        _uiMessage.setValue(null);
    }

    public LiveData<String> getBTConnectionDeviceName() {
        return _btConnectionDeviceName;
    }

    public LiveData<BTServerState> getBTServerStatus() {
        return _btServerStatus;
    }

    public void connectToDevice(BluetoothDevice bluetoothDevice) {
        btClient.connectToDevice(bluetoothDevice).thenAccept(btConnectResult -> {
            switch (btConnectResult.status) {
                case SUCCEEDED -> {
                    _btConnection.postValue(btConnectResult.connection);
                    _btConnectionDeviceName.postValue(btConnectResult.connection.getRemoteDeviceName());
                    _uiMessage.postValue(String.format("Successfully connected to %s",btConnectResult.connection.getRemoteDeviceName()));
                }
                case SKIPPED_ALREADY_CONNECTED ->
                        _uiMessage.postValue("Already Connected! Skipped.");
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
        btServer.startServerListening().thenAccept(btServerResult -> {
            switch (btServerResult.event) {
                case CONNECT_SUCCESS ->{
                    _btConnection.postValue(btServerResult.connection);
                    _btConnectionDeviceName.postValue(btServerResult.connection.getRemoteDeviceName());
                    _btServerStatus.postValue(BTServerState.OFF);
                    _uiMessage.postValue(String.format("Successfully auto accept connection from %s", btServerResult.connection.getRemoteDeviceName()));
                }
                case ON_SKIPPED -> {
                    _uiMessage.postValue("Already listening! Skipped");
                }
                case OFF -> {
                    _btServerStatus.postValue(BTServerState.OFF);
                }
                case FAILED -> {
                    _uiMessage.postValue("Failed to start the server");
                    _btServerStatus.postValue(BTServerState.OFF);
                }

            }
        });
        _uiMessage.setValue("BT server started, waiting for connection...");
    }

    public void closeBTServer() {
        if (btServer.closeBTServer().equals(BTServerEvent.OFF)) {
            _uiMessage.setValue("Successfully closed BT server.");
            _btServerStatus.setValue(BTServerState.OFF);
        } else {
            _uiMessage.setValue("Failed to close BT server.");
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        btClient.shutdown();
        btServer.shutdown();
    }
}
