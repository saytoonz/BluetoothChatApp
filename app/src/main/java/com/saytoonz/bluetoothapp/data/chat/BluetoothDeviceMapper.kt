package com.saytoonz.bluetoothapp.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.saytoonz.bluetoothapp.domain.chat.BluetoothDeviceDomain

@SuppressLint("MissingPermission") //Permission check already done before running this code so suppress
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return  BluetoothDeviceDomain(
        name = name,
        address = address

    )
}