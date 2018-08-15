package io.github.sithengineer.motoqueiro.hardware.capture

import android.location.Location

class LatLng {

  val lat: Double
  val lng: Double
  val timestamp: Long

  constructor(lat: Double, lng: Double, timestamp: Long) {
    this.lat = lat
    this.lng = lng
    this.timestamp = timestamp
  }

  constructor(location: Location) {
    this.lat = location.latitude
    this.lng = location.longitude
    this.timestamp = location.time
  }

  override fun toString(): String {
    return "lat=$lat, lng=$lng, timestamp=$timestamp"
  }
}
