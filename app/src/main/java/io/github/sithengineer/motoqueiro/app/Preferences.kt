package io.github.sithengineer.motoqueiro.app

import android.content.SharedPreferences

class Preferences(private val preferences: SharedPreferences) {

  var miBandAddress: String
    get() = preferences.getString(MI_BAND_ADDRESS, "")
    set(newMiBandAddress) = preferences.edit().putString(MI_BAND_ADDRESS, newMiBandAddress).apply()

  var rideId: Long
    get() = preferences.getLong(RIDE_ID, 0)
    set(rideId) = preferences.edit().putLong(RIDE_ID, rideId).apply()

  companion object {
    private const val MI_BAND_ADDRESS = "mi_band_address"
    private const val RIDE_ID = "ride_id"
  }
}
