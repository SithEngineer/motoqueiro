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

  fun saveGpsData(points: List<GpsPoint>): Completable

  fun saveGpsData(coordinates: GpsPoint): Completable

  fun getGpsData(rideId: Long): Single<List<GpsPoint>>

  fun saveAccelerometerData(points: List<AccelerometerPoint>): Completable

  fun saveAccelerometerData(point: AccelerometerPoint): Completable

  fun getAccelerometerData(rideId: Long): Single<List<AccelerometerPoint>>

  fun saveGyroscopeData(points: List<GyroscopePoint>): Completable

  fun saveGyroscopeData(point: GyroscopePoint): Completable

  fun getGyroscopeData(rideId: Long): Single<List<GyroscopePoint>>

  fun saveGravityData(points: List<GravityPoint>): Completable

  fun saveGravityData(point: GravityPoint): Completable

  fun getGravityData(rideId: Long): Single<List<GravityPoint>>

  fun saveHeartRateData(points: List<HeartRatePoint>): Completable

  fun saveHeartRateData(point: HeartRatePoint): Completable

  fun getHeartRateData(rideId: Long): Single<List<HeartRatePoint>>

  fun saveRide(ride: Ride): Completable

  fun getRide(rideId: Long): Single<Ride>

  fun getCompletedRides(): Single<List<Ride>>

  fun markCompleted(rideId: Long): Completable

  fun markSynced(rideId: Long): Completable
}
