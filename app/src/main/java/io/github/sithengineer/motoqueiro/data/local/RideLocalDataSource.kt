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

  override fun saveGpsData(points: List<GpsPoint>): Completable =
      Completable.fromAction { appDatabase.gpsPointDao().insertAll(points) }
          .subscribeOn(ioScheduler)

  override fun saveGpsData(coordinates: GpsPoint): Completable =
      Completable.fromAction { appDatabase.gpsPointDao().insert(coordinates) }
          .subscribeOn(ioScheduler)

  override fun getGpsData(rideId: Long): Single<List<GpsPoint>> =
      appDatabase.gpsPointDao().getAll(rideId = rideId).firstOrError()
          .subscribeOn(ioScheduler)

  override fun saveAccelerometerData(points: List<AccelerometerPoint>): Completable =
      Completable.fromAction { appDatabase.accelerometerPointDao().insertAll(points) }
          .subscribeOn(ioScheduler)

  override fun saveAccelerometerData(point: AccelerometerPoint): Completable =
      Completable.fromAction { appDatabase.accelerometerPointDao().insert(point) }
          .subscribeOn(ioScheduler)

  override fun getAccelerometerData(rideId: Long): Single<List<AccelerometerPoint>> =
      appDatabase.accelerometerPointDao().getAll(rideId = rideId).firstOrError()
          .subscribeOn(ioScheduler)

  override fun saveGyroscopeData(points: List<GyroscopePoint>): Completable =
      Completable.fromAction { appDatabase.gyroscopePointDao().insertAll(points) }
          .subscribeOn(ioScheduler)

  override fun saveGyroscopeData(point: GyroscopePoint): Completable =
      Completable.fromAction { appDatabase.gyroscopePointDao().insert(point) }
          .subscribeOn(ioScheduler)

  override fun getGyroscopeData(rideId: Long): Single<List<GyroscopePoint>> =
      appDatabase.gyroscopePointDao().getAll(rideId = rideId).firstOrError()
          .subscribeOn(ioScheduler)

  override fun saveGravityData(points: List<GravityPoint>): Completable =
      Completable.fromAction { appDatabase.gravityPointDao().insertAll(points) }
          .subscribeOn(ioScheduler)

  override fun saveGravityData(point: GravityPoint): Completable =
      Completable.fromAction { appDatabase.gravityPointDao().insert(point) }
          .subscribeOn(ioScheduler)

  override fun getGravityData(rideId: Long): Single<List<GravityPoint>> =
      appDatabase.gravityPointDao().getAll(rideId = rideId).firstOrError()
          .subscribeOn(ioScheduler)

  override fun saveHeartRateData(points: List<HeartRatePoint>): Completable =
      Completable.fromAction { appDatabase.heartRatePointDao().insertAll(points) }
          .subscribeOn(ioScheduler)

  override fun saveHeartRateData(point: HeartRatePoint): Completable =
      Completable.fromAction { appDatabase.heartRatePointDao().insert(point) }
          .subscribeOn(ioScheduler)

  override fun getHeartRateData(rideId: Long): Single<List<HeartRatePoint>> =
      appDatabase.heartRatePointDao().getAll(rideId).firstOrError()
          .subscribeOn(ioScheduler)

  override fun getCompletedRides(): Single<List<Ride>> = appDatabase.rideDao()
      .getAllInCompletedState(true)
      .firstOrError()
      .subscribeOn(ioScheduler)

  override fun getRide(rideId: Long): Single<Ride> =
      appDatabase.rideDao().getById(id = rideId)
          .subscribeOn(ioScheduler)

  override fun saveRide(ride: Ride): Completable =
      Completable.fromAction { appDatabase.rideDao().insert(ride) }
          .subscribeOn(ioScheduler)

  override fun markCompleted(rideId: Long): Completable =
      appDatabase.rideDao().getById(id = rideId).flatMapCompletable { ride ->
        return@flatMapCompletable Completable.fromAction {
          ride.isCompleted = true
          appDatabase.rideDao().update(ride)
        }
      }.subscribeOn(ioScheduler)

  override fun markSynced(rideId: Long): Completable =
      appDatabase.rideDao().getById(id = rideId).flatMapCompletable { ride ->
        return@flatMapCompletable Completable.fromAction {
          ride.isSynced = true
          appDatabase.rideDao().update(ride)
        }
      }.subscribeOn(ioScheduler)

}