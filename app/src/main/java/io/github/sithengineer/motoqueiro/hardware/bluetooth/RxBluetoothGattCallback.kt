package io.github.sithengineer.motoqueiro.hardware.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class RxBluetoothGattCallback : BluetoothGattCallback() {

  private val gattEventBehaviorSubject: BehaviorSubject<GattEvent> = BehaviorSubject.create()

  fun listen(): Observable<GattEvent> {
    return gattEventBehaviorSubject
  }

  override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
    if (newState == BluetoothProfile.STATE_CONNECTED) {
      gatt.discoverServices()
      gattEventBehaviorSubject.onNext(
          GattEvent(GattEvent.Type.CONNECT, gatt, status))
      return
    }
    gattEventBehaviorSubject.onNext(
        GattEvent(GattEvent.Type.DISCONNECT, gatt, status))
  }

  override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
    if (status == BluetoothGatt.GATT_SUCCESS) {
      gattEventBehaviorSubject.onNext(
          GattEvent(GattEvent.Type.SERVICE_DISCOVER, gatt, status))
    }
  }

  override fun onCharacteristicRead(gatt: BluetoothGatt,
      characteristic: BluetoothGattCharacteristic, status: Int) {
    gattEventBehaviorSubject.onNext(
        GattEvent(GattEvent.Type.READ, gatt, status, characteristic))
    super.onCharacteristicRead(gatt, characteristic, status)
  }

  override fun onCharacteristicWrite(gatt: BluetoothGatt,
      characteristic: BluetoothGattCharacteristic, status: Int) {
    gattEventBehaviorSubject.onNext(
        GattEvent(GattEvent.Type.WRITE, gatt, status, characteristic))
    super.onCharacteristicWrite(gatt, characteristic, status)
  }

  override fun onCharacteristicChanged(gatt: BluetoothGatt,
      characteristic: BluetoothGattCharacteristic) {
    gattEventBehaviorSubject.onNext(
        GattEvent(GattEvent.Type.NOTIFICATION, gatt, characteristic = characteristic))
    super.onCharacteristicChanged(gatt, characteristic)
  }
}
