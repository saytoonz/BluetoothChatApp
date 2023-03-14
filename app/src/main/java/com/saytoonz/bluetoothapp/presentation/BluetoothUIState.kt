package com.saytoonz.bluetoothapp.presentation

import com.saytoonz.bluetoothapp.domain.chat.BluetoothDeviceDomain

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
)
