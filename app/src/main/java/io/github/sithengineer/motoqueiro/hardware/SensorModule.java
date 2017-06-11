package io.github.sithengineer.motoqueiro.hardware;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.scope.RideScope;

@Module public class SensorModule {

  public static final String MI_BAND_DEPENDENCY = "miBandAddress";

  private final Context context;
  private final String miBandAddress;

  public SensorModule(Context context, String miBandAddress) {
    this.context = context;
    this.miBandAddress = miBandAddress;
  }

  @Provides @RideScope BluetoothAdapter provideBluetoothAdapter() {
    return ((BluetoothManager) context.getSystemService(
        Context.BLUETOOTH_SERVICE)).getAdapter();
  }

  @Provides @RideScope LocationManager provideLocationManager() {
    return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  @Provides @RideScope SensorManager provideSensorManager() {
    return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
  }

  @Provides @RideScope Gps.GpsStateListener provideGpsStateListener() {
    return new Gps.GpsStateListener();
  }

  @Provides @RideScope BluetoothDevice provideMiBandDevice(
      BluetoothAdapter bluetoohAdapter) {
    return bluetoohAdapter.getRemoteDevice(miBandAddress);
  }

  @Provides @RideScope MiBand provideMiBand(BluetoothDevice miBandDevice) {
    return new MiBand(context, miBandDevice);
  }
}
