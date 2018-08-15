package io.github.sithengineer.motoqueiro.data.local

import io.github.sithengineer.motoqueiro.data.RideDataSource
import io.github.sithengineer.motoqueiro.data.local.entity.AccelerometerPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GpsPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GravityPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GyroscopePoint
import io.github.sithengineer.motoqueiro.data.local.entity.HeartRatePoint
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class RideLocalDataSource(
    private val appDatabase: AppDatabase,
    private val ioScheduler: Scheduler
) : RideDataSource {
  override val completedRides: Single<List<Ride>>
    get() = TODO(
        "not implemented") //To change initializer of created properties use File | Settings | File Templates.

  override fun saveGpsData(rideId: Long, points: List<GpsPoint>): Completable {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveGpsData(rideId: Long, coordinates: GpsPoint): Single<Long> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getGpsData(rideId: Long): Single<List<GpsPoint>> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveAccelerometerData(rideId: Long,
      points: List<AccelerometerPoint>): Completable {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveAccelerometerData(rideId: Long, point: AccelerometerPoint): Single<Long> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAccelerometerData(rideId: Long): Single<List<AccelerometerPoint>> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveGyroscopeData(rideId: Long, points: List<GyroscopePoint>): Completable {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveGyroscopeData(rideId: Long, point: GyroscopePoint): Single<Long> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getGyroscopeData(rideId: Long): Single<List<GyroscopePoint>> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveGravityData(rideId: Long, points: List<GravityPoint>): Completable {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveGravityData(rideId: Long, point: GravityPoint): Single<Long> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getGravityData(rideId: Long): Single<List<GravityPoint>> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveHeartRateData(rideId: Long, points: List<HeartRatePoint>): Completable {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveHeartRateData(rideId: Long, point: HeartRatePoint): Single<Long> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getHeartRateData(rideId: Long): Single<List<HeartRatePoint>> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveRide(ride: Ride): Single<Long> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getRide(rideId: Long): Single<Ride> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun markCompleted(rideId: Long): Single<Boolean> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun markSynced(rideId: Long): Single<Boolean> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}