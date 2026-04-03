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
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTIOEvent;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTIOResult;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTIOStreamDataType;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerEvent;
import com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models.BTServerResult;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncRepository {

    public interface OnConnectionChangeListener {
        void onChanged(Map<String, BTConnection> connections);
    }

    public interface OnIOListener {
        void onBTInputEvent(BTIOResult btIOResult);
        void onBTOutputEvent(BTIOEvent btIOEvent);
    }

    private final BTClient btClient;
    private final BTServer btServer;
    private final Context context;
    private final Map<String, BTConnection> currentConnection = new ConcurrentHashMap<>();
    private final ExecutorService ioExecutor = Executors.newCachedThreadPool();
    private OnConnectionChangeListener listener;
    private OnIOListener ioListener;

    public SyncRepository(Context context) {
        this.context = context.getApplicationContext();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btClient = new BTClient(bluetoothAdapter);
        btServer = new BTServer(bluetoothAdapter);
    }

    public void setOnConnectionChangeListener(OnConnectionChangeListener listener) {
        this.listener = listener;
    }

    public void setOnIOListener(OnIOListener ioListener) {
        this.ioListener = ioListener;
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
            if (result.status() == BTConnectStatus.SUCCEEDED) {
                currentConnection.put(result.connection().getRemoteDeviceAddress(), result.connection());
                notifyConnectionChanged();
                startIOListener(result.connection());
            }
            return result;
        });
    }

    public BTServerEvent startServerListening() {
        return btServer.startServerListening();
    }

    public CompletableFuture<BTServerResult> acceptRemoteConnection() {
        return btServer.acceptRemoteConnection().thenApply(result -> {
            if (result.event() == BTServerEvent.CONNECT_SUCCESS) {
                currentConnection.put(result.connection().getRemoteDeviceAddress(), result.connection());
                notifyConnectionChanged();
                startIOListener(result.connection());
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

    public void sendMsg(BTConnection btConnection,String msg) {
        ioExecutor.submit(() -> {
            try {
                btConnection.sendMsg(msg);
                ioListener.onBTOutputEvent(BTIOEvent.SENT);
            } catch (IOException e) {
                ioListener.onBTOutputEvent(BTIOEvent.SEND_FAILED);
            }
        });
    }

    public void startIOListener(BTConnection btConnection) {
        ioExecutor.submit(() -> {
            while (btConnection.isConnected()){
                try {
                    byte[] bytes = btConnection.receiveMsg();
                    ioListener.onBTInputEvent(new BTIOResult(BTIOEvent.RECEIVED, BTIOStreamDataType.STRING,bytes));
                } catch (IOException e) {
                    ioListener.onBTInputEvent(new BTIOResult(BTIOEvent.RECEIVE_FAILED, null,null));
                    break;
                }
            }
        });
    }
}