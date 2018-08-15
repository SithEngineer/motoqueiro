package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RideDao {
  @Query("SELECT * FROM ride")
  fun getAll(): Flowable<List<Ride>>

  @Query("SELECT * FROM ride WHERE id = :id")
  fun getById(id: Long): Single<Ride>

  @Query("SELECT * FROM ride WHERE is_synced LIKE :isSynced")
  fun getAllBySyncState(isSynced: Boolean): Flowable<List<Ride>>

  @Query("SELECT * FROM ride WHERE is_completed LIKE :isCompleted")
  fun getAllInCompletedState(isCompleted: Boolean): Flowable<List<Ride>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg rides: Ride)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(ride: Ride)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(ride: Ride)

  @Delete
  fun delete(ride: Ride)

}