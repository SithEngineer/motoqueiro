package io.github.sithengineer.motoqueiro.data

import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.data.local.entity.AccelerometerPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GpsPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GravityPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GyroscopePoint
import io.github.sithengineer.motoqueiro.data.local.entity.HeartRatePoint
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import java.util.Calendar

class RideRepository(private val localDataSource: RideDataSource,
    private val remoteDataSource: RideDataSource,
    private val preferences: Preferences) {

  fun startRide(name: String): Single<Long> {
    return Single.just(
        Ride(event = name, initialTimestamp = Calendar.getInstance().timeInMillis)
    ).flatMap { ride ->
      localDataSource.saveRide(ride).doOnSuccess { rideId -> preferences.rideId = rideId }
    }
  }

  fun finishRide(rideId: Long): Single<Boolean> {
    return localDataSource.markCompleted(rideId)
  }

  fun saveHeartRate(rideId: Long, heartRate: Int): Single<Long> {
    val point = HeartRatePoint(rideId = rideId, beatsPerMinute = heartRate)
    return localDataSource.saveHeartRateData(rideId, point)
  }

  fun saveGpsCoordinate(rideId: Long, latitude: Double, longitude: Double): Single<Long> {
    val point = GpsPoint(rideId = rideId, latitude = latitude, longitude = longitude)
    return localDataSource.saveGpsData(rideId, point)
  }

  fun saveAccelerometerCapture(rideId: Long, xx: Float, yy: Float, zz: Float): Single<Long> {
    val point = AccelerometerPoint(rideId = rideId, x = xx, y = yy, z = zz)
    return localDataSource.saveAccelerometerData(rideId, point)
  }

  fun saveGyroscopeCapture(rideId: Long, xx: Float, yy: Float, zz: Float): Single<Long> {
    val point = GyroscopePoint(rideId = rideId, x = xx, y = yy, z = zz)
    return localDataSource.saveGyroscopeData(rideId, point)
  }

  fun saveGravityCapture(rideId: Long, xx: Float, yy: Float, zz: Float): Single<Long> {
    val point = GravityPoint(rideId = rideId, x = xx, y = yy, z = zz)
    return localDataSource.saveGravityData(rideId, point)
  }

  /**
   * Get all the non-sync'ed and finished rides and sends them to the server, for data
   * storage.
   * After sync, mark all the sent ride data as sync'ed
   */
  fun sync(): Completable {
    return localDataSource.completedRides
        .toObservable()
        .flatMapIterable { list -> list }
        .filter { ride -> !ride.isSynced }
        .flatMapSingle { ride ->
          remoteDataSource.saveRide(ride)
              .flatMap { localDataSource.markSynced(ride.id) }
              .doOnError { err -> Timber.e(err) }
        }
        .toList()
        .ignoreElement()
  }
}
