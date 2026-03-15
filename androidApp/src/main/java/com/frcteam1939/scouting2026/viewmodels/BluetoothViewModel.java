package com.frcteam1939.scouting2026.viewmodels;

import android.app.Application;
import android.bluetooth.BluetoothDevice;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTController;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTServerStatus;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTUtils;
import java.util.List;

public class BluetoothViewModel extends AndroidViewModel {
    private final BTController btController;
    private final MutableLiveData<BTServerStatus> _bTServerStatus = new MutableLiveData<>(BTServerStatus.OFF);
    private final MutableLiveData<String> _connectedDevice;
    private final MutableLiveData<List<BluetoothDevice>> _pairedDevices;
    private final MutableLiveData<String> _uiMessage = new MutableLiveData<>(null);


    public BluetoothViewModel(@NonNull Application application) {
        super(application);

        btController = new BTController(application.getApplicationContext());
        _connectedDevice = new MutableLiveData<>(btController.getConnectedDeviceName());
        _pairedDevices = new MutableLiveData<>(BTUtils.getPairedDevice(application.getApplicationContext()));
    }

    public LiveData<List<BluetoothDevice>> getPairedDevices() {
        return _pairedDevices;
    }

    public void refreshPairedDevices() {
        _pairedDevices.setValue(BTUtils.getPairedDevice(getApplication().getApplicationContext()));
    }

    public LiveData<String> getConnectedDevice() {
        return _connectedDevice;
    }

    public LiveData<BTServerStatus> getBTServerStatus() {
        return _bTServerStatus;
    }

    public LiveData<String> getUiMessage() {
        return _uiMessage;
    }

    public void clearUiMessage() {
        _uiMessage.setValue(null);
    }

    public void connectToDevice(BluetoothDevice bluetoothDevice) {
        btController.connectToDevice(bluetoothDevice).thenAccept(success -> {
            switch (success) {
                case SUCCEEDED -> {
                    _connectedDevice.postValue(btController.getConnectedDeviceName());
                    _uiMessage.postValue("Successfully Connected to device");
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

    public void startBTServer() {
        if (_bTServerStatus.getValue() == BTServerStatus.LISTENING) {
            _uiMessage.setValue("BT server already listening!");
            return;
        }
        _bTServerStatus.setValue(BTServerStatus.LISTENING);
        btController.startServerListening().thenAccept(_bTServerStatus::postValue);
        _uiMessage.setValue("BT server started");
    }

    public void closeBTServer() {
        if (btController.closeBTServer()) {
            _uiMessage.setValue("Successfully closed BT server.");
        } else {
            _uiMessage.setValue("Failed to close BT server.");
        }
    }
}
