package io.github.sithengineer.motoqueiro.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.github.sithengineer.motoqueiro.data.local.dao.AccelerometerPointDao
import io.github.sithengineer.motoqueiro.data.local.dao.GpsPointDao
import io.github.sithengineer.motoqueiro.data.local.dao.GravityPointDao
import io.github.sithengineer.motoqueiro.data.local.dao.GyroscopePointDao
import io.github.sithengineer.motoqueiro.data.local.dao.HeartRatePointDao
import io.github.sithengineer.motoqueiro.data.local.dao.RideDao
import io.github.sithengineer.motoqueiro.data.local.dao.UserDao
import io.github.sithengineer.motoqueiro.data.local.entity.AccelerometerPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GpsPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GravityPoint
import io.github.sithengineer.motoqueiro.data.local.entity.GyroscopePoint
import io.github.sithengineer.motoqueiro.data.local.entity.HeartRatePoint
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.github.sithengineer.motoqueiro.data.local.entity.User

@Database(
    entities = [AccelerometerPoint::class, GpsPoint::class, GravityPoint::class,
      GyroscopePoint::class, HeartRatePoint::class, Ride::class, User::class],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun accelerometerPointDao(): AccelerometerPointDao
  abstract fun gpsPointDao(): GpsPointDao
  abstract fun gravityPointDao(): GravityPointDao
  abstract fun gyroscopePointDao(): GyroscopePointDao
  abstract fun heartRatePointDao(): HeartRatePointDao
  abstract fun rideDao(): RideDao
  abstract fun userDao(): UserDao
}