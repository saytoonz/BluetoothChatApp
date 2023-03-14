package com.saytoonz.bluetoothapp.presentation

import com.saytoonz.bluetoothapp.domain.chat.BluetoothDeviceDomain

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<BluetoothDeviceDomain> = emptyList(),
)
