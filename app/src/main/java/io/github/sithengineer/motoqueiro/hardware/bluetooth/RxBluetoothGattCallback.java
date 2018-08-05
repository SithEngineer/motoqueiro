package io.github.sithengineer.motoqueiro.hardware.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class RxBluetoothGattCallback extends BluetoothGattCallback {

  private BehaviorSubject<GattEvent> gattEventBehaviorSubject;

  public RxBluetoothGattCallback() {
    gattEventBehaviorSubject = BehaviorSubject.create();
  }

  public Observable<GattEvent> listen() {
    return gattEventBehaviorSubject;
  }

  @Override
  public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
    if (newState == BluetoothProfile.STATE_CONNECTED) {
      gatt.discoverServices();
      gattEventBehaviorSubject.onNext(
          new GattEvent(GattEvent.Type.CONNECT, gatt, status));
      return;
    }
    gattEventBehaviorSubject.onNext(
        new GattEvent(GattEvent.Type.DISCONNECT, gatt, status));
  }

  @Override public void onServicesDiscovered(BluetoothGatt gatt, int status) {
    if (status == BluetoothGatt.GATT_SUCCESS) {
      gattEventBehaviorSubject.onNext(
          new GattEvent(GattEvent.Type.SERVICE_DISCOVER, gatt, status));
    }
  }

  @Override public void onCharacteristicRead(BluetoothGatt gatt,
      BluetoothGattCharacteristic characteristic, int status) {
    gattEventBehaviorSubject.onNext(
        new GattEvent(GattEvent.Type.READ, gatt, characteristic, status));
    super.onCharacteristicRead(gatt, characteristic, status);
  }

  @Override public void onCharacteristicWrite(BluetoothGatt gatt,
      BluetoothGattCharacteristic characteristic, int status) {
    gattEventBehaviorSubject.onNext(
        new GattEvent(GattEvent.Type.WRITE, gatt, characteristic, status));
    super.onCharacteristicWrite(gatt, characteristic, status);
  }

  @Override public void onCharacteristicChanged(BluetoothGatt gatt,
      BluetoothGattCharacteristic characteristic) {
    gattEventBehaviorSubject.onNext(
        new GattEvent(GattEvent.Type.NOTIFICATION, gatt, characteristic));
    super.onCharacteristicChanged(gatt, characteristic);
  }
}
