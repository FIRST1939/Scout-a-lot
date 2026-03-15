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
    private BluetoothServerSocket bluetoothServerSocket;
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
    public String getConnectedDeviceName() {
        return connectedDeviceName;
    }

    @SuppressLint("MissingPermission")
    public synchronized CompletableFuture<BTConnectStatus> connectToDevice(BluetoothDevice targetDevice) {
        if (isConnected()) {
            Log.w(TAG,"Already connected, skipping");
            return CompletableFuture.completedFuture(BTConnectStatus.SKIPPED_ALREADY_CONNECTED);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                if (bluetoothAdapter == null) {
                    Log.e(TAG, "Bluetooth adapter is null");
                    return BTConnectStatus.FAILED_NO_ADAPTER;
                }
                bluetoothSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                try {
                    bluetoothAdapter.cancelDiscovery();
                } catch (SecurityException securityException) {
                    Log.w(TAG, "No BLUETOOTH_SCAN permission, skip cancelDiscovery()", securityException);
                }
                bluetoothSocket.connect();
                updateConnectedDeviceName();
                return BTConnectStatus.SUCCEEDED;
            } catch (SecurityException e) {
                Log.e(TAG, "Permission Needed", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
                return BTConnectStatus.FAILED_NO_PERMISSION;
            } catch (IOException e) {
                Log.e(TAG, "God damn Connection Failed, server on?", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
                return BTConnectStatus.FAILED_TO_CONNECT;
            }
        },bluetoothConnectExecutor);
    }

    @SuppressLint("MissingPermission")
    public synchronized CompletableFuture<BTServerStatus> startServerListening() {
        if (isConnected()) {
            return CompletableFuture.completedFuture(BTServerStatus.OFF);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Sync", MY_UUID);
                bluetoothSocket = bluetoothServerSocket.accept();
                updateConnectedDeviceName();
                // Needs remove
                MAIN_HANDLER.post(() -> {
                            String text = String.format("Bluetooth successfully connected with device %s.",bluetoothSocket.getRemoteDevice() != null ? bluetoothSocket.getRemoteDevice().getName() : null);
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        }
                );
                bluetoothServerSocket.close();
                return BTServerStatus.CONNECTED;
            } catch (IOException e) {
                Log.e(TAG, "ERROR Listening", e);
                return BTServerStatus.OFF;
            }
        },bluetoothListenExecutor);
    }

    public Boolean closeBTServer() {
        try {
            bluetoothServerSocket.close();
            return true;
        } catch (NullPointerException | IOException e) {
            Log.e(TAG, "Failed to close BluetoothServerSocket", e);
            return false;
        }
    }
}

