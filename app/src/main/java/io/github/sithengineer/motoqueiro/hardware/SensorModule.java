package io.github.sithengineer.motoqueiro.hardware;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;

@Module public class SensorModule {

  private final Context context;
  private final BluetoothAdapter bluetoothAdapter;

  public SensorModule(Activity activity) {
    this.context = activity;
    this.bluetoothAdapter =
        ((BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
  }

  @Provides BluetoothAdapter provideBluetoothAdapter() {
    return bluetoothAdapter;
  }

  @Provides LocationManager provideLocationManager() {
    return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  @Provides SensorManager provideSensorManager() {
    return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
  }

  @Provides Gps.GpsStateListener provideGpsStateListener() {
    return new Gps.GpsStateListener();
  }

  @Provides BluetoothDevice provideMiBandDevice(BluetoothAdapter bluetoohAdapter) {
    return bluetoohAdapter.getRemoteDevice("F0:E3:C8:01:11:0D");
  }

  @Provides MiBand provideMiBand(BluetoothDevice miBandDevice) {
    return new MiBand(context, miBandDevice);
  }
}
