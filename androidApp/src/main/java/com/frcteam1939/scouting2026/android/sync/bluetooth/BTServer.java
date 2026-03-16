package com.frcteam1939.scouting2026.android.sync.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("MissingPermission")
public class BTServer {
    private final ExecutorService bluetoothServerExecutor = Executors.newSingleThreadExecutor();
    private static final UUID MY_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-0800200c9a66");
    private static final String TAG = "BTServer";
    private volatile boolean isListening = false;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothSocket bluetoothSocket;

    public BTServer(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    // Auto accept
    public CompletableFuture<BTServerResult> startServerListening() {
        if (isListening) {
            return CompletableFuture.completedFuture(
                    new BTServerResult(BTServerEvent.ON_SKIPPED, null)
            );
        }
        isListening = true;
        return CompletableFuture.supplyAsync(() -> {
            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Sync", MY_UUID);
                bluetoothSocket = bluetoothServerSocket.accept();
                bluetoothServerSocket.close();
                isListening = false;
                return new BTServerResult(BTServerEvent.CONNECT_SUCCESS,new BTConnection(bluetoothSocket));
            } catch (IOException e) {
                if (isListening) {
                    Log.e(TAG, "ERROR Listening", e);
                    isListening = false;
                    return new BTServerResult(BTServerEvent.FAILED, null);
                } else {
                    return new BTServerResult(BTServerEvent.OFF, null);
                }
            }
        },bluetoothServerExecutor);
    }

    public BTServerEvent closeBTServer() {
        if (!isListening) return BTServerEvent.OFF_SKIPPED;
        isListening = false;
        if (bluetoothServerSocket != null) {
            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close BluetoothServerSocket", e);
                return BTServerEvent.OFF_FAILED;
            }
        }
        return BTServerEvent.OFF;
    }
}
