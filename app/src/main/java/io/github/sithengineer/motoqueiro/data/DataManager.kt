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
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

class DataManager(private val accelerometer: Accelerometer, private val gyroscope: Gyroscope,
    private val gravity: Gravity, private val gps: Gps,
    private val miBand: MiBandService, private val rideRepo: RideRepository,
    private val preferences: Preferences) {

  private val rideId: Long?
    get() = preferences.rideId

  fun gatherData(): Observable<Void> {
    // gps data generator observable
    val generateGpsObservable = gps.listen().flatMapSingle { pos ->
      handleGpsPositionCapture(pos)
    }.map<Void> { null }

    // accelerometer sensor data generator observable
    val generateAccelObservable = accelerometer.listen()
        .flatMapSingle { accelData -> handleAccelerometerCapture(accelData) }
        .onErrorReturn { err ->
          Timber.e(err)
          0
        }
        .map { null }

    // gyroscope sensor data generator observable
    val generateGravityObservable = gravity.listen()
        .flatMapSingle { gravityData -> handleGravityCapture(gravityData) }
        .onErrorReturn { err ->
          Timber.e(err)
          0
        }
        .map<Void> { null }

    // gyroscope sensor data generator observable
    val generateGyroObservable = gyroscope.listen()
        .flatMapSingle { gyroData -> handleGyroscopeCapture(gyroData) }
        .onErrorReturn { err ->
          Timber.e(err)
          0
        }
        .map { null }

    // miband heart rate sensor data generator observable
    val generateHeartRateObservable = miBand.listen()
        .flatMapSingle { heartData -> handleMiBandCapture(heartData) }
        .onErrorReturn { err ->
          Timber.e(err)
          0
        }
        .map { null }

    return Observable.merge(
        arrayListOf(
            generateGpsObservable,
            generateAccelObservable,
            generateGyroObservable,
            generateGravityObservable,
            generateHeartRateObservable
        )
    )
  }

  private fun handleMiBandCapture(bandData: MiBandData): Single<Long> {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveHeartRate(currentRideId, bandData.heartRateBpm)
    } else {
      Single.error(EmptyRideId())
    }
  }

  private fun handleGpsPositionCapture(capture: LatLng): Single<Long> {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveGpsCoordinate(currentRideId, capture.lat, capture.lng)
    } else {
      Single.error(EmptyRideId())
    }
  }

  private fun handleAccelerometerCapture(capture: RelativeCoordinates): Single<Long> {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveAccelerometerCapture(currentRideId, capture.xx, capture.yy,
          capture.zz)
    } else {
      Single.error(EmptyRideId())
    }
  }

  private fun handleGravityCapture(capture: RelativeCoordinates): Single<Long> {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveGravityCapture(currentRideId, capture.xx, capture.yy,
          capture.zz)
    } else {
      Single.error(EmptyRideId())
    }
  }

  private fun handleGyroscopeCapture(capture: RelativeCoordinates): Single<Long> {
    val currentRideId = rideId
    return if (currentRideId != null) {
      rideRepo.saveGyroscopeCapture(currentRideId, capture.xx, capture.yy,
          capture.zz)
    } else {
      Single.error(EmptyRideId())
    }
  }
}
