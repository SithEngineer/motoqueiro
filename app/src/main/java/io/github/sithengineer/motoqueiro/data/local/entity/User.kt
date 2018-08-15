package io.github.sithengineer.motoqueiro.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "is_logged_in") val isLoggedIn: Boolean,
    @ColumnInfo(name = "name") val name: String = ""
)
