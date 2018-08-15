package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.GyroscopePoint
import io.reactivex.Flowable

@Dao
interface GyroscopePointDao {
  @Query("SELECT * FROM gyroscope_point")
  fun getAll(): Flowable<List<GyroscopePoint>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg points: GyroscopePoint)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(gpsPoint: GyroscopePoint)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(gpsPoint: GyroscopePoint)

  @Delete
  fun delete(gpsPoint: GyroscopePoint)
}