package io.github.sithengineer.motoqueiro.hardware.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public class GattEvent {

  private final Type type;
  private final BluetoothGatt gatt;
  private final BluetoothGattCharacteristic characteristic;
  private final int status;

  public GattEvent(Type type, BluetoothGatt gatt,
      BluetoothGattCharacteristic characteristic, int status) {
    this.type = type;
    this.gatt = gatt;
    this.characteristic = characteristic;
    this.status = status;
  }

  public GattEvent(Type type, BluetoothGatt gatt,
      BluetoothGattCharacteristic characteristic) {
    this(type, gatt, characteristic, 0);
  }

  public GattEvent(Type type, BluetoothGatt gatt, int status) {
    this(type, gatt, null, status);
  }

  public Type getType() {
    return type;
  }

  public BluetoothGatt getGatt() {
    return gatt;
  }

  public BluetoothGattCharacteristic getCharacteristic() {
    return characteristic;
  }

  public int getStatus() {
    return status;
  }

  public enum Type {
    CONNECT, DISCONNECT, READ, WRITE, NOTIFICATION, SERVICE_DISCOVER
  }
}
