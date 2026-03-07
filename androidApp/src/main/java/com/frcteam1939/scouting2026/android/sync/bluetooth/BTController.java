package com.frcteam1939.scouting2026.android.sync.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.UUID;

public class BTController {
    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private static final String TAG = "BTController";
    private static final UUID MY_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-0800200c9a66");
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    // Constructor
    public BTController(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    public boolean isConnected() {
        try {
            return bluetoothSocket.isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    public String getSafeConnectedDeviceName() {
        try {
            return bluetoothSocket.getRemoteDevice().getName();
        } catch (NullPointerException e) {
            return "None";
        }
    }

    @SuppressLint("MissingPermission")
    public synchronized void connectToDevice(BluetoothDevice targetDevice) {

        if (isConnected()) {
            Log.w(TAG,"Already connected, skipping");
            Toast.makeText(context, "Already connected!", Toast.LENGTH_SHORT).show();

            return;
        }

        new Thread(() -> {
            try {
                if (bluetoothAdapter == null) {
                    Log.e(TAG, "Bluetooth adapter is null");
                    return;
                }

                bluetoothSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);

                try {
                    bluetoothAdapter.cancelDiscovery();
                } catch (SecurityException securityException) {
                    Log.w(TAG, "No BLUETOOTH_SCAN permission, skip cancelDiscovery()", securityException);
                }

                bluetoothSocket.connect();
                MAIN_HANDLER.post(() -> {
                            String text = String.format("Bluetooth successfully connected with device %s.",bluetoothSocket.getRemoteDevice() != null ? bluetoothSocket.getRemoteDevice().getName() : null);
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        }
                );

            } catch (SecurityException e) {
                Log.e(TAG, "Permission Needed", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
            } catch (IOException e) {
                Log.e(TAG, "God damn Connection Failed, server on?", e);
                MAIN_HANDLER.post(() -> {
                    Toast.makeText(context, "Connection Failed, Make sure server is listening", Toast.LENGTH_SHORT).show();
                });
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
            }
        }).start();
    }

    @SuppressLint("MissingPermission")
    public synchronized void startServerListening() {

        if (isConnected()) {
            return;
        }

        new Thread(() -> {
            BluetoothServerSocket bluetoothServerSocket;

            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Sync", MY_UUID);
                bluetoothSocket = bluetoothServerSocket.accept();
                MAIN_HANDLER.post(() -> {
                    String text = String.format("Bluetooth successfully connected with device %s.",bluetoothSocket.getRemoteDevice() != null ? bluetoothSocket.getRemoteDevice().getName() : null);
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        }
                );
                bluetoothServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ERROR Listening", e);
            }
        }).start();
    }
}
