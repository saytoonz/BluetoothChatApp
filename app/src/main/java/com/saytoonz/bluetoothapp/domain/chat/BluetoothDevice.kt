package com.saytoonz.bluetoothapp.domain.chat



typealias BluetoothDeviceDomain = BluetoothDeviceData

data class BluetoothDeviceData( // Since BluetoothDevice == the name in android.bluetooth SDK lets alias this class
    val name: String?, // Device name
    val address: String // Mac address
)
