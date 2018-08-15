package io.github.sithengineer.motoqueiro.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "gyroscope_point",
    foreignKeys = [
      ForeignKey(
          entity = Ride::class,
          parentColumns = arrayOf("id"),
          childColumns = arrayOf("ride_id"),
          onDelete = ForeignKey.CASCADE)
    ],
    indices = [
      Index("ride_id")
    ]
)
data class GyroscopePoint(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "ride_id") var rideId: Long,
    @ColumnInfo(name = "x") val x: Float,
    @ColumnInfo(name = "y") val y: Float,
    @ColumnInfo(name = "z") val z: Float
)