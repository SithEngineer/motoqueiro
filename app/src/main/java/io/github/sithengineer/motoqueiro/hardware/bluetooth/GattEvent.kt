package io.github.sithengineer.motoqueiro.hardware.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

class GattEvent @JvmOverloads constructor(val type: Type, val gatt: BluetoothGatt,
    val characteristic: BluetoothGattCharacteristic?, val status: Int = 0) {

  constructor(type: Type, gatt: BluetoothGatt, status: Int) : this(type, gatt, null, status) {}

  enum class Type {
    CONNECT, DISCONNECT, READ, WRITE, NOTIFICATION, SERVICE_DISCOVER
  }
}
