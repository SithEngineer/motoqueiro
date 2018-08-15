package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.HeartRatePoint
import io.reactivex.Flowable

@Dao
interface HeartRatePointDao {
  @Query("SELECT * FROM heart_rate_point")
  fun getAll(): Flowable<List<HeartRatePoint>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg points: HeartRatePoint)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(gpsPoint: HeartRatePoint)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(gpsPoint: HeartRatePoint)

  @Delete
  fun delete(gpsPoint: HeartRatePoint)
}