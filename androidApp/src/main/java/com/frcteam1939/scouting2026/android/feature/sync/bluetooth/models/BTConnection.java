package com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;

@SuppressLint("MissingPermission")
public class BTConnection {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private static final String TAG = "BTConnection";

    public BTConnection(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        bluetoothDevice = bluetoothSocket.getRemoteDevice();
    }

    public boolean isConnected() {
        return bluetoothSocket.isConnected();
    }

    public String getRemoteDeviceName() {
        String name = bluetoothDevice.getName();
        return name != null ? name : "Unknown";
    }

    public String getRemoteDeviceAddress() {
        return bluetoothDevice.getAddress();
    }

    public void closeSocket() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.w(TAG, "Failed to close socket. May already closed", e);
        }
    }
}
