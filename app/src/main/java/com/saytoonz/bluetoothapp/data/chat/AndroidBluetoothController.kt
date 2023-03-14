package com.saytoonz.bluetoothapp.data.chat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.saytoonz.bluetoothapp.domain.chat.BluetoothController
import com.saytoonz.bluetoothapp.domain.chat.BluetoothDeviceDomain
import com.saytoonz.bluetoothapp.domain.chat.ConnectionResult
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.security.Permission
import java.util.UUID


@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
) : BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList());
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList());
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()


    private val foundDeviceReceiver = FoundDeviceReceiver { bluetoothDevice ->
        _scannedDevices.update { devices ->
            val newDevice = bluetoothDevice.toBluetoothDeviceDomain()
            if (newDevice in devices) devices else devices + newDevice
        }
    }

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {
        updatePairedDevices()
    }

    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        updatePairedDevices()

        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){
            return
        }
        bluetoothAdapter?.cancelDiscovery()
    }


    override fun startBluetoothServer(): Flow<ConnectionResult> {
        return  flow {
            if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)){
                throw SecurityException("No BLUETOOTH_CONNECT Permission")
            }

            currentServerSocket =      bluetoothAdapter
                ?.listenUsingRfcommWithServiceRecord(
                    "Bluetooth_Chat",
                    UUID.fromString(SERVICE_UUID),
                )

            var shouldLoop = true
            while (shouldLoop){
                try {
                    currentServerSocket?.accept()
                }catch (ex: IOException){

                }
            }

        }
    }

    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {

    }

    override fun closeConnection() {
        TODO("Not yet implemented")
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
    }


    private fun updatePairedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map {
                it.toBluetoothDeviceDomain()
            }
            ?.also { devices ->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        const val SERVICE_UUID = "d8e83672-d5d0-4800-b1bb-f7f6c357ba20"
    }
}