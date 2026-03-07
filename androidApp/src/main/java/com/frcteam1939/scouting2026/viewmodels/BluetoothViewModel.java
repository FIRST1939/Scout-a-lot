package com.frcteam1939.scouting2026.viewmodels;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.frcteam1939.scouting2026.android.sync.bluetooth.BTController;
import com.frcteam1939.scouting2026.android.sync.bluetooth.BTUtils;

import java.util.List;

public class BluetoothViewModel extends AndroidViewModel {
    private final BTController btController;
    private final MutableLiveData<Boolean> _listeningStatus = new MutableLiveData<>(false);
    private final MutableLiveData<String> _btControllerDeviceName;
    private final MutableLiveData<List<BluetoothDevice>> _pairedDevices;

    public BluetoothViewModel(@NonNull Application application) {
        super(application);
        btController = new BTController(application.getApplicationContext());
        _btControllerDeviceName = new MutableLiveData<>(btController.getSafeName().getValue());
        _pairedDevices = new MutableLiveData<>(BTUtils.getPairedDevice(application.getApplicationContext()));
        startListeningOnce();
    }

    public void startListeningOnce() {
        if (Boolean.TRUE.equals(_listeningStatus.getValue())) return;
        _listeningStatus.setValue(true);
        btController.startServerListening();
    }

}
