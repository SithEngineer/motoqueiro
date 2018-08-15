package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.GpsPoint
import io.reactivex.Flowable

@Dao
interface GpsPointDao {
  @Query("SELECT * FROM gps_point WHERE ride_id = :rideId")
  fun getAll(rideId: Long): Flowable<List<GpsPoint>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(points: List<GpsPoint>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(gpsPoint: GpsPoint)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(gpsPoint: GpsPoint)

  @Delete
  fun delete(gpsPoint: GpsPoint)
}