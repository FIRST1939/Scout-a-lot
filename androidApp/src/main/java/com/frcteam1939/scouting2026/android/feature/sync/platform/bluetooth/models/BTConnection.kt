package com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

@SuppressLint("MissingPermission")
class BTConnection(private val bluetoothSocket: BluetoothSocket) {
    private val bluetoothDevice: BluetoothDevice = bluetoothSocket.remoteDevice
    private val dataOutputStream: DataOutputStream
    private val dataInputStream: DataInputStream
    private val TAG: String = remoteDeviceAddress

    init {
        var tmpIn: InputStream? = null
        var tmpOut: OutputStream? = null

        try {
            tmpIn = bluetoothSocket.inputStream
        } catch (e: IOException) {
            Log.e(TAG, "Error occurred when creating input stream", e)
        }
        try {
            tmpOut = bluetoothSocket.outputStream
        } catch (e: IOException) {
            Log.e(TAG, "Error occurred when creating output stream", e)
        }

        val inputStream = tmpIn
        val outputStream = tmpOut
        dataOutputStream = DataOutputStream(outputStream)
        dataInputStream = DataInputStream(inputStream)
    }

    val isConnected: Boolean
        get() = bluetoothSocket.isConnected

    val remoteDeviceName: String
        get() {
            val name = bluetoothDevice.name
            return name ?: "Unknown"
        }

    val remoteDeviceAddress: String
        get() = bluetoothDevice.address

    fun closeSocket() {
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            Log.w(TAG, "Failed to close socket. May already closed", e)
        }
    }

    @Throws(IOException::class)
    fun sendMsg(msg: String) {
        val bytes = msg.toByteArray(StandardCharsets.UTF_8)
        dataOutputStream.writeInt(bytes.size)
        dataOutputStream.write(bytes)
        dataOutputStream.flush()
    }

    @Throws(IOException::class)
    fun receiveMsg(): ByteArray {
        val length = dataInputStream.readInt()
        val bytes = ByteArray(length)
        dataInputStream.readFully(bytes)
        return bytes
    }
}
