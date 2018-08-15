package io.github.sithengineer.motoqueiro.hardware.gps

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import timber.log.Timber

open class GpsStateListener : LocationListener {

  override fun onLocationChanged(location: Location) {
    // does nothing
  }

  override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
    // does nothing
  }

  override fun onProviderEnabled(s: String) {
    Timber.i("location provider enabled")
  }

  override fun onProviderDisabled(s: String) {
    Timber.w("location provider disabled")
  }

  fun isLocationServiceActive(locationManager: LocationManager): Boolean {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
  }
}
