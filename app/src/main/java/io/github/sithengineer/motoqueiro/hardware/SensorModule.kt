package io.github.sithengineer.motoqueiro.hardware

import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener
import io.github.sithengineer.motoqueiro.scope.RideScope

@Module
class SensorModule(private val context: Context) {

  @Provides
  @RideScope
  internal fun provideLocationManager(): LocationManager {
    return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  }

  @Provides
  @RideScope
  internal fun provideSensorManager(): SensorManager {
    return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
  }

  @Provides
  @RideScope
  internal fun provideGpsStateListener(): GpsStateListener {
    return GpsStateListener()
  }
}
