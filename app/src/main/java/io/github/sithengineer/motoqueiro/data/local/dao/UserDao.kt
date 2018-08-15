package io.github.sithengineer.motoqueiro.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.github.sithengineer.motoqueiro.data.local.entity.User
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UserDao {

  @Query("SELECT * FROM user")
  fun getAll(): Flowable<List<User>>

  @Query("SELECT * FROM user WHERE id IN (:userIds)")
  fun loadAllByIds(userIds: IntArray): Flowable<List<User>>

  @Query("SELECT * FROM user WHERE name LIKE :name LIMIT 1")
  fun findByName(name: String): Single<User>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(vararg users: User)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(user: User)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(user: User)

  @Delete
  fun delete(user: User)

}