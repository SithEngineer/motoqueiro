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

  override fun getCompletedRides(): Single<List<Ride>> = Single.error(NoSuchMethodException())

  override fun saveGpsData(points: List<GpsPoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveGpsData(coordinates: GpsPoint): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun getGpsData(rideId: Long): Single<List<GpsPoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveAccelerometerData(points: List<AccelerometerPoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveAccelerometerData(point: AccelerometerPoint): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun getAccelerometerData(rideId: Long): Single<List<AccelerometerPoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveGyroscopeData(points: List<GyroscopePoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveGyroscopeData(point: GyroscopePoint): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun getGyroscopeData(rideId: Long): Single<List<GyroscopePoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveGravityData(points: List<GravityPoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveGravityData(point: GravityPoint): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun getGravityData(rideId: Long): Single<List<GravityPoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveHeartRateData(points: List<HeartRatePoint>): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun saveHeartRateData(point: HeartRatePoint): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun getHeartRateData(rideId: Long): Single<List<HeartRatePoint>> {
    return Single.error(NoSuchMethodException())
  }

  override fun saveRide(ride: Ride): Completable {
    return rideWebService.upload(ride)
  }

  override fun getRide(rideId: Long): Single<Ride> {
    return Single.error(NoSuchMethodException())
  }

  override fun markCompleted(rideId: Long): Completable {
    return Completable.error(NoSuchMethodException())
  }

  override fun markSynced(rideId: Long): Completable {
    return Completable.error(NoSuchMethodException())
  }
}
