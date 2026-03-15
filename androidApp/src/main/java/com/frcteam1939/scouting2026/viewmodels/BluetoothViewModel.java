package com.frcteam1939.scouting2026.viewmodels;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

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
    private final Context context;

    public BluetoothViewModel(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
        btController = new BTController(application.getApplicationContext());
        _connectedDevice = new MutableLiveData<>(btController.getConnectedDeviceName());
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

    public LiveData<BTServerStatus> getBTServerStatus() {
        return _bTServerStatus;
    }

    public void connectToDevice(BluetoothDevice bluetoothDevice) {
        btController.connectToDevice(bluetoothDevice).thenAccept(success -> {
            if (success) {
                _connectedDevice.postValue(btController.getConnectedDeviceName());
            }
        });
    }

    public void startBTServer() {
        if (_bTServerStatus.getValue() == BTServerStatus.LISTENING) return;

        _bTServerStatus.setValue(BTServerStatus.LISTENING);

        btController.startServerListening().thenAccept(_bTServerStatus::postValue);
    }

    public void closeBTServer() {
        if (btController.closeBTServer()) {
            Toast.makeText(context, "Successfully closed BT server.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to close BT server.", Toast.LENGTH_SHORT).show();
        }
    }
}
