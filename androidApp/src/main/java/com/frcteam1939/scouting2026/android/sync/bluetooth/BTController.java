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

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BTController {
    private final Context context;
    private final ExecutorService bluetoothConnectExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService bluetoothListenExecutor = Executors.newSingleThreadExecutor();

    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private static final String TAG = "BTController";
    private static final UUID MY_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-0800200c9a66");
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private String connectedDeviceName = "None";

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
    private void updateConnectedDeviceName() {
        try {
            connectedDeviceName = bluetoothSocket.getRemoteDevice().getName();
        } catch (NullPointerException ignored) {}
    }

    @SuppressLint("MissingPermission")
    public String getSafeConnectedDeviceName() {
        return connectedDeviceName;
    }

    @SuppressLint("MissingPermission")
    public synchronized CompletableFuture<Boolean> connectToDevice(BluetoothDevice targetDevice) {

        if (isConnected()) {
            Log.w(TAG,"Already connected, skipping");
            Toast.makeText(context, "Already connected!", Toast.LENGTH_SHORT).show();
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                if (bluetoothAdapter == null) {
                    Log.e(TAG, "Bluetooth adapter is null");
                    return false;
                }

                bluetoothSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);

                try {
                    bluetoothAdapter.cancelDiscovery();
                } catch (SecurityException securityException) {
                    Log.w(TAG, "No BLUETOOTH_SCAN permission, skip cancelDiscovery()", securityException);
                }
                bluetoothSocket.connect();
                updateConnectedDeviceName();
                MAIN_HANDLER.post(() -> {
                            String text = String.format("Bluetooth successfully connected with device %s.",bluetoothSocket.getRemoteDevice() != null ? bluetoothSocket.getRemoteDevice().getName() : null);
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        }
                );
                return true;

            } catch (SecurityException e) {
                Log.e(TAG, "Permission Needed", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
                return false;
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
                return false;
            }
        },bluetoothConnectExecutor);
    }

    @SuppressLint("MissingPermission")
    public synchronized CompletableFuture<BTServerState> startServerListening() {

        if (isConnected()) {
            return CompletableFuture.completedFuture(BTServerState.OFF);
        }

        return CompletableFuture.supplyAsync(() -> {
            BluetoothServerSocket bluetoothServerSocket;

            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Sync", MY_UUID);
                bluetoothSocket = bluetoothServerSocket.accept();
                updateConnectedDeviceName();
                MAIN_HANDLER.post(() -> {
                            String text = String.format("Bluetooth successfully connected with device %s.",bluetoothSocket.getRemoteDevice() != null ? bluetoothSocket.getRemoteDevice().getName() : null);
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        }
                );
                bluetoothServerSocket.close();
                return BTServerState.CONNECTED;
            } catch (IOException e) {
                Log.e(TAG, "ERROR Listening", e);
                return BTServerState.OFF;
            }
        },bluetoothListenExecutor);
    }
}

