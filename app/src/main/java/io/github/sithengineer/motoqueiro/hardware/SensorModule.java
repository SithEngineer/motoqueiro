package io.github.sithengineer.motoqueiro.hardware;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.scope.RideScope;

@Module public class SensorModule {

  private final Context context;

  public SensorModule(Context context) {
    this.context = context;
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

}
