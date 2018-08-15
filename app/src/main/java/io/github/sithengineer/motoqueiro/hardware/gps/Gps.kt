package io.github.sithengineer.motoqueiro.hardware.gps

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import io.github.sithengineer.motoqueiro.hardware.HardwareObservable
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng
import io.reactivex.Observable
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Gps @Inject constructor(private val locationManager: LocationManager) :
    HardwareObservable<LatLng> {

  fun mock(): Observable<LatLng> {
    return Observable.interval(0, MIN_TIME, TimeUnit.MILLISECONDS)
        .map { count ->
          LatLng(count / 0.3, count / 0.7,
              Calendar.getInstance().timeInMillis)
        }
  }

  @SuppressLint("MissingPermission")
  override fun listen(): Observable<LatLng> {
    return Observable.create { subscriber ->
      val listener = object : GpsStateListener() {
        override fun onLocationChanged(location: Location) {
          super.onLocationChanged(location)
          if (!subscriber.isDisposed) {
            val latLng = LatLng(location)
            Timber.v("gps: %s", latLng.toString())
            subscriber.onNext(latLng)
          }
        }
      }
      // needs to check permissions on the fly (android 6+)
      subscriber.setCancellable { locationManager.removeUpdates(listener) }
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,
          MIN_DISTANCE, listener)
    }
  }

  companion object {
    const val MIN_TIME = 200L
    const val MIN_DISTANCE = 0.2f
  }
}
