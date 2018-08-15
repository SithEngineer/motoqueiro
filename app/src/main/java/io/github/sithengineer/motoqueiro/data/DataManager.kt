package io.github.sithengineer.motoqueiro.data

import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.exception.EmptyRideId
import io.github.sithengineer.motoqueiro.hardware.Accelerometer
import io.github.sithengineer.motoqueiro.hardware.Gravity
import io.github.sithengineer.motoqueiro.hardware.Gyroscope
import io.github.sithengineer.motoqueiro.hardware.MiBandService
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates
import io.github.sithengineer.motoqueiro.hardware.gps.Gps
import io.reactivex.Completable

class DataManager(private val accelerometer: Accelerometer, private val gyroscope: Gyroscope,
    private val gravity: Gravity, private val gps: Gps,
    private val miBand: MiBandService, private val rideRepo: RideRepository,
    private val preferences: Preferences) {

  private val rideId: Long?
    get() = preferences.rideId

  fun gatherData(): Completable {
    // gps data generator observable
    val generateGpsObservable = gps.listen().flatMapCompletable { pos ->
      handleGpsPositionCapture(pos)
    }

    // accelerometer sensor data generator observable
    val generateAccelObservable = accelerometer.listen()
        .flatMapCompletable { accelData -> handleAccelerometerCapture(accelData) }

    // gyroscope sensor data generator observable
    val generateGravityObservable = gravity.listen()
        .flatMapCompletable { gravityData -> handleGravityCapture(gravityData) }

    // gyroscope sensor data generator observable
    val generateGyroObservable = gyroscope.listen()
        .flatMapCompletable { gyroData -> handleGyroscopeCapture(gyroData) }

    // miband heart rate sensor data generator observable
    val generateHeartRateObservable = miBand.listen()
        .flatMapCompletable { heartData -> handleMiBandCapture(heartData) }

    return Completable.merge(
        arrayListOf(
            generateGpsObservable,
            generateAccelObservable,
            generateGyroObservable,
            generateGravityObservable,
            generateHeartRateObservable
        )
    )
  }

  private fun handleMiBandCapture(bandData: MiBandData): Completable {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveHeartRate(currentRideId, bandData.heartRateBpm)
    } else {
      Completable.error(EmptyRideId())
    }
  }

  private fun handleGpsPositionCapture(capture: LatLng): Completable {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveGpsCoordinate(currentRideId, capture.lat, capture.lng)
    } else {
      Completable.error(EmptyRideId())
    }
  }

  private fun handleAccelerometerCapture(capture: RelativeCoordinates): Completable {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveAccelerometerCapture(currentRideId, capture.xx, capture.yy,
          capture.zz)
    } else {
      Completable.error(EmptyRideId())
    }
  }

  private fun handleGravityCapture(capture: RelativeCoordinates): Completable {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveGravityCapture(currentRideId, capture.xx, capture.yy,
          capture.zz)
    } else {
      Completable.error(EmptyRideId())
    }
  }

  private fun handleGyroscopeCapture(capture: RelativeCoordinates): Completable {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveGyroscopeCapture(currentRideId, capture.xx, capture.yy,
          capture.zz)
    } else {
      Completable.error(EmptyRideId())
    }
  }
}
