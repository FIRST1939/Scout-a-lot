package com.frcteam1939.scouting2026.android.sync.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BTUtils {
    public static final UUID BT_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-0800200c9a66");

    public static List<BluetoothDevice> getPairedDevice(Context context) {
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return Collections.emptyList();
        }
        final BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            return Collections.emptyList();
        }

        final boolean hasPermission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ?
                (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) :
                (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) return Collections.emptyList();

        List<BluetoothDevice> pairedDevicesList;
        try {
            pairedDevicesList = List.copyOf(bluetoothAdapter.getBondedDevices());
            return pairedDevicesList;
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

}
