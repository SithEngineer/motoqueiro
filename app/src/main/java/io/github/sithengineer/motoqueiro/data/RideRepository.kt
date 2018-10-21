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
import org.threeten.bp.Instant

class RideRepository(private val localDataSource: RideDataSource,
    private val remoteDataSource: RideDataSource,
    private val preferences: Preferences) {

  fun startRide(name: String): Single<Ride> {
    return Single.just(
        Ride(event = name, initialTimestamp = Instant.now().toEpochMilli())
    ).flatMap { ride ->
      localDataSource
          .saveRide(ride)
          .doOnComplete { preferences.rideId = ride.id }
          .toSingle { ride }
    }
  }

  fun finishRide(rideId: Long): Completable {
    return localDataSource.markCompleted(rideId)
  }

  fun saveHeartRate(rideId: Long, heartRate: Int): Completable {
    val point = HeartRatePoint(rideId = rideId, beatsPerMinute = heartRate)
    return localDataSource.saveHeartRateData(point)
  }

  fun saveGpsCoordinate(rideId: Long, latitude: Double, longitude: Double): Completable {
    val point = GpsPoint(rideId = rideId, latitude = latitude, longitude = longitude)
    return localDataSource.saveGpsData(point)
  }

  fun saveAccelerometerCapture(rideId: Long, xx: Float, yy: Float, zz: Float): Completable {
    val point = AccelerometerPoint(rideId = rideId, x = xx, y = yy, z = zz)
    return localDataSource.saveAccelerometerData(point)
  }

  fun saveGyroscopeCapture(rideId: Long, xx: Float, yy: Float, zz: Float): Completable {
    val point = GyroscopePoint(rideId = rideId, x = xx, y = yy, z = zz)
    return localDataSource.saveGyroscopeData(point)
  }

  fun saveGravityCapture(rideId: Long, xx: Float, yy: Float, zz: Float): Completable {
    val point = GravityPoint(rideId = rideId, x = xx, y = yy, z = zz)
    return localDataSource.saveGravityData(point)
  }

  /**
   * Get all the non-sync'ed and finished rides and sends them to the server, for data
   * storage.
   * After sync, mark all the sent ride data as sync'ed
   */
  fun sync(): Completable {
    return localDataSource.getCompletedRides()
        .toObservable()
        .flatMapIterable { list -> list }
        .filter { ride -> !ride.isSynced }
        .flatMapCompletable { ride ->
          remoteDataSource.saveRide(ride).andThen(localDataSource.markSynced(ride.id))
        }
  }
}
