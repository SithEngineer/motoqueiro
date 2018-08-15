package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.AccelerometerPoint
import io.reactivex.Flowable

@Dao
interface AccelerometerPointDao {
  @Query("SELECT * FROM accelerometer_point")
  fun getAll(): Flowable<List<AccelerometerPoint>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg points: AccelerometerPoint)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(gpsPoint: AccelerometerPoint)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(gpsPoint: AccelerometerPoint)

  @Delete
  fun delete(gpsPoint: AccelerometerPoint)
}