package com.frcteam1939.scouting2026.android.feature.sync.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnectResult;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnectStatus;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnection;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("MissingPermission")
public class BTClient {
    private final ExecutorService bluetoothClientExecutor = Executors.newSingleThreadExecutor();
    private static final String TAG = "BTClient";
    private final BluetoothAdapter bluetoothAdapter;

    public BTClient(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public CompletableFuture<BTConnectResult> connectToDevice(BluetoothDevice bluetoothDevice) {
        
        return CompletableFuture.supplyAsync(() -> {
            BluetoothSocket bluetoothSocket = null;
            try {
                if (bluetoothAdapter == null) {
                    Log.e(TAG, "Bluetooth adapter is null");
                    return new BTConnectResult(BTConnectStatus.FAILED_NO_ADAPTER,null);
                }
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(BTUtils.BT_UUID);
                try {
                    bluetoothAdapter.cancelDiscovery();
                } catch (SecurityException securityException) {
                    Log.w(TAG, "No BLUETOOTH_SCAN permission, skip cancelDiscovery()", securityException);
                }
                bluetoothSocket.connect();
                return new BTConnectResult(BTConnectStatus.SUCCEEDED,new BTConnection(bluetoothSocket));
            } catch (SecurityException e) {
                Log.e(TAG, "Permission Needed", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
                return new BTConnectResult(BTConnectStatus.FAILED_NO_PERMISSION,null);
            } catch (IOException e) {
                Log.e(TAG, "God damn Connection Failed, server on?", e);
                try {
                    if (bluetoothSocket != null) {
                        bluetoothSocket.close();
                    }
                } catch (IOException closeException) {
                    Log.e(TAG, "Close Connection Failed", closeException);
                }
                return new BTConnectResult(BTConnectStatus.FAILED_TO_CONNECT,null);
            }
        },bluetoothClientExecutor);
    }

    public void shutdown() {
        bluetoothClientExecutor.shutdownNow();
    }
}
