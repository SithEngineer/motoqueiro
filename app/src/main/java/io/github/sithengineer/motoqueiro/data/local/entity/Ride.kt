package io.github.sithengineer.motoqueiro.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ride")
data class Ride constructor(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,

    @ColumnInfo(name = "event")
    val event: String,

    @ColumnInfo(name = "initial_timestamp")
    val initialTimestamp: Long,

    @ColumnInfo(name = "final_timestamp")
    var finalTimestamp: Long = 0,

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    var isSynced: Boolean = false
) {

  fun setEndTimestamp(time: Long) {
    finalTimestamp = time
    isCompleted = true
  }
}
