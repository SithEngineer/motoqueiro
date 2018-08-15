package io.github.sithengineer.motoqueiro.data.remote

import io.github.sithengineer.motoqueiro.data.RideDataSource
import io.github.sithengineer.motoqueiro.data.local.entity.AccelerometerPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GpsPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GravityPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GyroscopePoint
import io.github.sithengineer.motoqueiro.data.local.entity.HeartRatePoint
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.github.sithengineer.motoqueiro.network.RideWebService
import io.reactivex.Completable
import io.reactivex.Single

class RideRemoteDataSource(private val rideWebService: RideWebService) : RideDataSource {

  override val completedRides: Single<List<Ride>>
    get() = Single.error(NoSuchMethodException())

  override fun saveGpsData(rideId: Long, points: List<GpsPoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveGpsData(rideId: Long, coordinates: GpsPoint): Single<Long> {
    return Single.error(NoSuchMethodException())
  }

  override fun getGpsData(rideId: Long): Single<List<GpsPoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveAccelerometerData(rideId: Long, points: List<AccelerometerPoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveAccelerometerData(rideId: Long, point: AccelerometerPoint): Single<Long> {
    return Single.error(NoSuchMethodException())
  }

  override fun getAccelerometerData(rideId: Long): Single<List<AccelerometerPoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveGyroscopeData(rideId: Long, points: List<GyroscopePoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveGyroscopeData(rideId: Long, point: GyroscopePoint): Single<Long> {
    return Single.error(NoSuchMethodException())
  }

  override fun getGyroscopeData(rideId: Long): Single<List<GyroscopePoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveGravityData(rideId: Long, points: List<GravityPoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveGravityData(rideId: Long, point: GravityPoint): Single<Long> {
    return Single.error(NoSuchMethodException())
  }

  override fun getGravityData(rideId: Long): Single<List<GravityPoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveHeartRateData(rideId: Long, points: List<HeartRatePoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveHeartRateData(rideId: Long, point: HeartRatePoint): Single<Long> {
    return Single.error(NoSuchMethodException())
  }

  override fun getHeartRateData(rideId: Long): Single<List<HeartRatePoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveRide(ride: Ride): Single<Long> {
    return rideWebService.upload(ride).toSingleDefault(ride.id)
  }

  override fun getRide(rideId: Long): Single<Ride> {
    return Single.error(NoSuchMethodException())
  }

  override fun markCompleted(rideId: Long): Single<Boolean> {
    return Single.error(NoSuchMethodException())
  }

  override fun markSynced(rideId: Long): Single<Boolean> {
    return Single.error(NoSuchMethodException())
  }
}
