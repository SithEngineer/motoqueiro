package io.github.sithengineer.motoqueiro.data

import io.github.sithengineer.motoqueiro.data.local.entity.AccelerometerPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GpsPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GravityPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GyroscopePoint
import io.github.sithengineer.motoqueiro.data.local.entity.HeartRatePoint
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.reactivex.Completable
import io.reactivex.Single

interface RideDataSource {

  val completedRides: Single<List<Ride>>
  fun saveGpsData(rideId: Long, points: List<GpsPoint>): Completable

  fun saveGpsData(rideId: Long, coordinates: GpsPoint): Single<Long>

  fun getGpsData(rideId: Long): Single<List<GpsPoint>>

  fun saveAccelerometerData(rideId: Long, points: List<AccelerometerPoint>): Completable

  fun saveAccelerometerData(rideId: Long, point: AccelerometerPoint): Single<Long>

  fun getAccelerometerData(rideId: Long): Single<List<AccelerometerPoint>>

  fun saveGyroscopeData(rideId: Long, points: List<GyroscopePoint>): Completable

  fun saveGyroscopeData(rideId: Long, point: GyroscopePoint): Single<Long>

  fun getGyroscopeData(rideId: Long): Single<List<GyroscopePoint>>

  fun saveGravityData(rideId: Long, points: List<GravityPoint>): Completable

  fun saveGravityData(rideId: Long, point: GravityPoint): Single<Long>

  fun getGravityData(rideId: Long): Single<List<GravityPoint>>

  fun saveHeartRateData(rideId: Long, points: List<HeartRatePoint>): Completable

  fun saveHeartRateData(rideId: Long, point: HeartRatePoint): Single<Long>

  fun getHeartRateData(rideId: Long): Single<List<HeartRatePoint>>

  fun saveRide(ride: Ride): Single<Long>

  fun getRide(rideId: Long): Single<Ride>

  fun markCompleted(rideId: Long): Single<Boolean>

  fun markSynced(rideId: Long): Single<Boolean>
}
