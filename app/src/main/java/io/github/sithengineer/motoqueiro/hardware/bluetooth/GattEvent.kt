package io.github.sithengineer.motoqueiro.hardware.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

data class GattEvent(
    val type: Type,
    val gatt: BluetoothGatt,
    val status: Int = 0,
    val characteristic: BluetoothGattCharacteristic? = null
) {

  enum class Type {
    CONNECT, DISCONNECT, READ, WRITE, NOTIFICATION, SERVICE_DISCOVER
  }
}
