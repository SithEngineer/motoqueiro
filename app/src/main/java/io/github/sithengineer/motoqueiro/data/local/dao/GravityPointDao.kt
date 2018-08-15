package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.GravityPoint
import io.reactivex.Flowable

@Dao
interface GravityPointDao {
  @Query("SELECT * FROM gravity_point")
  fun getAll(): Flowable<List<GravityPoint>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg points: GravityPoint)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(gpsPoint: GravityPoint)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(gpsPoint: GravityPoint)

  @Delete
  fun delete(gpsPoint: GravityPoint)
}