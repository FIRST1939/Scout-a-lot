package com.frcteam1939.scouting2026.android.sync.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("MissingPermission")
public class BTServer {
    private final ExecutorService bluetoothServerExecutor = Executors.newSingleThreadExecutor();
    private static final String TAG = "BTServer";
    private volatile boolean isListening = false;
    private final BluetoothAdapter bluetoothAdapter;
    private volatile BluetoothServerSocket bluetoothServerSocket;

    public BTServer(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    // Auto accept
    public BTServerEvent startServerListening() {
        if (isListening) {
            return BTServerEvent.ON_SKIPPED;
        }
        isListening = true;
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Sync", BTUtils.BT_UUID);
            return BTServerEvent.ON;
        } catch (IOException e) {
            Log.e(TAG, "ERROR starting server", e);
            isListening = false;
            return BTServerEvent.ON_FAILED;
        }
    }

    public CompletableFuture<BTServerResult> acceptRemoteConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BluetoothSocket bluetoothSocket;
                bluetoothSocket = bluetoothServerSocket.accept();
                return new BTServerResult(BTServerEvent.CONNECT_SUCCESS,new BTConnection(bluetoothSocket));
            } catch (IOException e) {
                Log.e(TAG,"Error while accepting connection", e);
                return new BTServerResult(BTServerEvent.CONNECT_FAILED,null);
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

    public void shutdown() {
        closeBTServer();
        bluetoothServerExecutor.shutdownNow();
    }

}
