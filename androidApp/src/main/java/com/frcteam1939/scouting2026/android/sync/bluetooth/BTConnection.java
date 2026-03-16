package com.frcteam1939.scouting2026.android.sync.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

@SuppressLint("MissingPermission")
public class BTConnection {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;

    public BTConnection(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        bluetoothDevice = bluetoothSocket.getRemoteDevice();
    }

    public boolean isConnected() {
        return bluetoothSocket.isConnected();
    }

    public String getRemoteDeviceName() {
        return bluetoothDevice.getName();
    }

    public String getRemoteDeviceAddress() {
        return bluetoothDevice.getAddress();
    }

    public void closeSocket() {
        try {
            bluetoothSocket.close();
        } catch (Exception ignored) {}
    }
}
