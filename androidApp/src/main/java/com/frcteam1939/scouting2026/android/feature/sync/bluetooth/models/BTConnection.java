package com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SuppressLint("MissingPermission")
public class BTConnection {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private final String TAG;

    public BTConnection(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        bluetoothDevice = bluetoothSocket.getRemoteDevice();
        TAG = getRemoteDeviceAddress();
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        InputStream inputStream = tmpIn;
        OutputStream outputStream = tmpOut;
        dataOutputStream = new DataOutputStream(outputStream);
        dataInputStream = new DataInputStream(inputStream);
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

    public void sendMsg(String msg) throws IOException {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        dataOutputStream.flush();
    }

    public byte[] receiveMsg() throws IOException {
        int length = dataInputStream.readInt();
        byte[] bytes = new byte[length];
        dataInputStream.readFully(bytes);
        return bytes;
    }
}
