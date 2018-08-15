package io.github.sithengineer.motoqueiro.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "heart_rate_point")
data class HeartRatePoint(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "ride_id") var rideId: Long,
    @ColumnInfo(name = "beats_per_minute") var beatsPerMinute: Int
)
