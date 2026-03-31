package com.frcteam1939.scouting2026.android.feature.sync;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.BTClient;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.BTServer;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.BTUtils;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnectResult;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnectStatus;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTConnection;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerEvent;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SyncRepository {
    private final BTClient btClient;
    private final BTServer btServer;
    private final Context context;
    private BTConnection currentConnection;

    public SyncRepository(Context context) {
        this.context = context.getApplicationContext();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btClient = new BTClient(bluetoothAdapter);
        btServer = new BTServer(bluetoothAdapter);
    }

    public CompletableFuture<BTConnectResult> connectToDevice(BluetoothDevice device) {
        if (currentConnection != null
                && device.getAddress().equals(currentConnection.getRemoteDeviceAddress())) {
            return CompletableFuture.completedFuture(
                    new BTConnectResult(BTConnectStatus.SKIPPED_ALREADY_CONNECTED, null));
        }

        return btClient.connectToDevice(device).thenApply(result -> {
            if (result.status == BTConnectStatus.SUCCEEDED) {
                closeCurrentConnection();
                currentConnection = result.connection;
            }
            return result;
        });
    }

    public BTServerEvent startServerListening() {
        return btServer.startServerListening();
    }

    public CompletableFuture<BTServerResult> acceptRemoteConnection() {
        return btServer.acceptRemoteConnection().thenApply(result -> {
            if (result.event == BTServerEvent.CONNECT_SUCCESS) {
                closeCurrentConnection();
                currentConnection = result.connection;
            }
            return result;
        });
    }

    public BTServerEvent closeBTServer() {
        return btServer.closeBTServer();
    }

    /**
     * @return device name if a connection was closed, null if no connection existed
     */
    public String closeExistingConnection() {
        if (currentConnection != null) {
            String name = currentConnection.getRemoteDeviceName();
            currentConnection.closeSocket();
            currentConnection = null;
            return name;
        }
        return null;
    }

    private void closeCurrentConnection() {
        if (currentConnection != null) {
            currentConnection.closeSocket();
            currentConnection = null;
        }
    }

    public List<BluetoothDevice> getPairedDevices() {
        return BTUtils.getPairedDevice(context);
    }

    public void shutdown() {
        closeCurrentConnection();
        btClient.shutdown();
        btServer.shutdown();
    }
}
