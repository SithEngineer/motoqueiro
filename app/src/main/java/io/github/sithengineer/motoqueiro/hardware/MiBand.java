package io.github.sithengineer.motoqueiro.hardware;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.RxBluetoothGattCallback;
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import io.github.sithengineer.motoqueiro.util.Consts;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import timber.log.Timber;

public class MiBand implements HardwareObservable<MiBandData> {

  private final Context context;
  private final BluetoothDevice miBandDevice;
  private final RxBluetoothGattCallback callBack;
  private BluetoothGatt myGatBand;

  public MiBand(Context context, BluetoothDevice miBandDevice) {
    this.context = context;
    this.miBandDevice = miBandDevice;
    this.callBack = new RxBluetoothGattCallback();
  }

  @Override public Observable<MiBandData> listen() {

    final Observable<MiBandData> getMiBandFromCallback = callBack.listen()
        .filter(event -> event.getCharacteristic() != null)
        .map(gattEvent -> {
          BluetoothGattCharacteristic characteristic = gattEvent.getCharacteristic();
          UUID alertUUID = characteristic.getUuid();
          if (alertUUID.equals(Consts.UUID_NOTIFICATION_HEARTRATE)) {
            final byte heartBeat = characteristic.getValue()[1];
            return new MiBandData(heartBeat);
          } else if (alertUUID.equals(Consts.UUID_BUTTON_TOUCH)) {
            return new MiBandData(true);
          }
          return new MiBandData();
        });

    return writeHeartBeatRequestInterval().publish()
        .refCount()
        .flatMap(__ -> getMiBandFromCallback)
        .doOnNext(data -> Timber.v("MiBand: %s", data.toString()));
  }

  private Observable<Void> writeHeartBeatRequestInterval() {
    Observable<Void> setupNotificationsObservable =
        Observable.timer(5, TimeUnit.SECONDS).map(__ -> {
          getNotifications(Consts.UUID_SERVICE_MIBAND2_SERVICE, Consts.UUID_BUTTON_TOUCH);
          Timber.d("setup to get touch notifications");
          return null;
        });

    Observable<Void> setupHeartRateObservable =
        Observable.timer(5, TimeUnit.SECONDS).map(__ -> {
          getNotificationsWithDescriptor(Consts.UUID_SERVICE_HEARTBEAT,
              Consts.UUID_NOTIFICATION_HEARTRATE,
              Consts.UUID_DESCRIPTOR_UPDATE_NOTIFICATION);
          Timber.d("setup to get heart rate data");
          return null;
        });

    Observable<Void> sendHearRateReadRequests =
        Observable.interval(15, TimeUnit.SECONDS).map(__ -> {
          writeData(Consts.UUID_SERVICE_HEARTBEAT,
              Consts.UUID_START_HEARTRATE_CONTROL_POINT, Consts.BYTE_NEW_HEART_RATE_SCAN);
          return null;
        });

    return setupNotificationsObservable.concatWith(setupHeartRateObservable)
        .concatWith(sendHearRateReadRequests)
        .doOnSubscribe(() -> connect())
        .doOnUnsubscribe(() -> disconnect());
  }

  private void connect() {
    myGatBand = miBandDevice.connectGatt(context, true, callBack);
    Timber.i("connected");
  }

  private void getNotifications(UUID service, UUID characteristic) {
    if (myGatBand == null) {
      Timber.w("can't get notifications"); // not initialized?
      return;
    }

    BluetoothGattService bluetoothGattService = myGatBand.getService(service);
    if (bluetoothGattService != null) {

      BluetoothGattCharacteristic myGatChar =
          bluetoothGattService.getCharacteristic(characteristic);
      if (myGatChar != null) {
        myGatBand.setCharacteristicNotification(myGatChar, true);
      }
    }
  }

  /**
   * Get notification but also set descriptor to Enable notification. You need to wait
   * couple of
   * seconds before you could use it (at least in the mi band 2)
   */
  private void getNotificationsWithDescriptor(UUID service, UUID Characteristics,
      UUID Descriptor) {
    if (myGatBand == null) {
      Timber.w("can't get notifications"); // not initialized?
      return;
    }

    BluetoothGattService myGatService = myGatBand.getService(service);
    if (myGatService != null) {

      BluetoothGattCharacteristic myGatChar =
          myGatService.getCharacteristic(Characteristics);
      if (myGatChar != null) {

        myGatBand.setCharacteristicNotification(myGatChar, true);

        BluetoothGattDescriptor myDescriptor = myGatChar.getDescriptor(Descriptor);
        if (myDescriptor != null) {
          myDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
          myGatBand.writeDescriptor(myDescriptor);
        }
      }
    }
  }

  private void disconnect() {
    myGatBand.disconnect();
    myGatBand.close();
    myGatBand = null;
    Timber.i("disconnected");
  }

  private void writeData(UUID service, UUID characteristic, byte[] data) {
    if (myGatBand == null) {
      Timber.w("can't read data"); // not initialized maybe?
      return;
    }

    BluetoothGattService myGatService = myGatBand.getService(service);
    if (myGatService != null) {
      BluetoothGattCharacteristic myGatChar =
          myGatService.getCharacteristic(characteristic);
      if (myGatChar != null) {
        myGatChar.setValue(data);
        myGatBand.writeCharacteristic(myGatChar);
      }
    }
  }
}
