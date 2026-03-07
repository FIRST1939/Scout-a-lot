package com.frcteam1939.scouting2026.android.sync.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;

public class BTController {
    private static final String TAG = "BTController";
    private static final UUID MY_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-0800200c9a66");
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;

    public BTController() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public interface ConnectResultCallback {
        void onResult(boolean success);
    }

    @SuppressLint("MissingPermission")
    public void connectToDevice(BluetoothDevice targetDevice, ConnectResultCallback callback) {
        new Thread(() -> {
            boolean isSuccess = false;
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
                isSuccess = true;

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
                Log.e(TAG, "Connection Failed", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
            } finally {
                if (callback != null) {
                    final boolean result = isSuccess;
                    MAIN_HANDLER.post(() -> callback.onResult(result));
                }
            }
        }).start();
    }

    @SuppressLint("MissingPermission")
    public void startServerListening() {

        new Thread(() -> {
            BluetoothServerSocket bluetoothServerSocket;
            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Sync", MY_UUID);
                bluetoothSocket = bluetoothServerSocket.accept();
                bluetoothServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ERROR Listening", e);
            }
        }).start();
    }
}
