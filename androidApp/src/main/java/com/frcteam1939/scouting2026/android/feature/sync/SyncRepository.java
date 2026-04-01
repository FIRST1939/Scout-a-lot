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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SyncRepository {

    public interface OnConnectionChangeListener {
        void onChanged(Map<String, BTConnection> connections);
    }

    private final BTClient btClient;
    private final BTServer btServer;
    private final Context context;
    private final Map<String, BTConnection> currentConnection = new ConcurrentHashMap<>();
    private OnConnectionChangeListener listener;

    public SyncRepository(Context context) {
        this.context = context.getApplicationContext();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btClient = new BTClient(bluetoothAdapter);
        btServer = new BTServer(bluetoothAdapter);
    }

    public void setOnConnectionChangeListener(OnConnectionChangeListener listener) {
        this.listener = listener;
    }

    private void notifyConnectionChanged() {
        if (listener != null) {
            listener.onChanged(currentConnection);
        }
    }

    public CompletableFuture<BTConnectResult> connectToDevice(BluetoothDevice device) {
        if (currentConnection.containsKey(device.getAddress())) {
            return CompletableFuture.completedFuture(
                    new BTConnectResult(BTConnectStatus.SKIPPED_ALREADY_CONNECTED, null));
        }

        return btClient.connectToDevice(device).thenApply(result -> {
            if (result.status == BTConnectStatus.SUCCEEDED) {
                currentConnection.put(result.connection.getRemoteDeviceAddress(), result.connection);
                notifyConnectionChanged();
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
                currentConnection.put(result.connection.getRemoteDeviceAddress(), result.connection);
                notifyConnectionChanged();
            }
            return result;
        });
    }

    public BTServerEvent closeBTServer() {
        return btServer.closeBTServer();
    }

    public String closeExistingConnection(BTConnection btConnection) {
        BTConnection old = currentConnection.remove(btConnection.getRemoteDeviceAddress());
        if (old != null) {
            old.closeSocket();
            notifyConnectionChanged();
            return btConnection.getRemoteDeviceName();
        }
        return null;
    }

    private void closeAllConnection() {
        for (BTConnection btConnection : currentConnection.values()) {
            btConnection.closeSocket();
        }
    }

    public List<BluetoothDevice> getPairedDevices() {
        return BTUtils.getPairedDevice(context);
    }

    public void shutdown() {
        closeAllConnection();
        btClient.shutdown();
        btServer.shutdown();
    }
}
