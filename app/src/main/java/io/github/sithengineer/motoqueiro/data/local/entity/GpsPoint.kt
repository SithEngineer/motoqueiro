package io.github.sithengineer.motoqueiro.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "gps_point")
data class GpsPoint(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "ride_id") var rideId: Long,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double
)
