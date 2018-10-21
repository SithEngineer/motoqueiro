package io.github.sithengineer.motoqueiro.data

import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.hardware.Accelerometer
import io.github.sithengineer.motoqueiro.hardware.Gravity
import io.github.sithengineer.motoqueiro.hardware.Gyroscope
import io.github.sithengineer.motoqueiro.hardware.MiBandService
import io.github.sithengineer.motoqueiro.hardware.gps.Gps
import io.github.sithengineer.motoqueiro.scope.RideScope

@Module
class DataCaptureModule {

  @Provides
  @RideScope
  internal fun providesDataManager(accelerometer: Accelerometer,
      gyroscope: Gyroscope, gravity: Gravity, gps: Gps, miBand: MiBandService,
      rideRepo: RideRepository,
      preferences: Preferences): DataManager {
    return DataManager(accelerometer, gyroscope, gravity, gps, miBand, rideRepo, preferences)
  }
}
