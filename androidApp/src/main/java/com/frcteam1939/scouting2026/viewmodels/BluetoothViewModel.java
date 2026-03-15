package com.frcteam1939.scouting2026.viewmodels;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTController;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTServerState;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTUtils;
import java.util.List;

import kotlinx.coroutines.scheduling.NanoTimeSource;

public class BluetoothViewModel extends AndroidViewModel {
    private final BTController btController;
    private final MutableLiveData<BTServerState> _bTServerStatus = new MutableLiveData<>(BTServerState.OFF);
    private final MutableLiveData<String> _connectedDevice;
    private final MutableLiveData<List<BluetoothDevice>> _pairedDevices;

    public BluetoothViewModel(@NonNull Application application) {
        super(application);
        btController = new BTController(application.getApplicationContext());
        _connectedDevice = new MutableLiveData<>(btController.getSafeConnectedDeviceName());
        _pairedDevices = new MutableLiveData<>(BTUtils.getPairedDevice(application.getApplicationContext()));
    }

    public LiveData<List<BluetoothDevice>> getPairedDevices() {
        return _pairedDevices;
    }

    public void refreshPairedDevices() {
        _pairedDevices.setValue(
                BTUtils.getPairedDevice(getApplication().getApplicationContext())
        );
    }

    public LiveData<String> getConnectedDevice() {
        return _connectedDevice;
    }

    public LiveData<BTServerState> getBTServerStatus() {
        return _bTServerStatus;
    }

    public void connectToDevice(BluetoothDevice bluetoothDevice) {
        btController.connectToDevice(bluetoothDevice).thenAccept(success -> {
            if (success) {
                _connectedDevice.postValue(btController.getSafeConnectedDeviceName());
            }
        });
    }

    public void startListening() {
        if (_bTServerStatus.getValue() == BTServerState.LISTENING) return;

        _bTServerStatus.setValue(BTServerState.LISTENING);

        btController.startServerListening().thenAccept(_bTServerStatus::postValue);
    }
}
