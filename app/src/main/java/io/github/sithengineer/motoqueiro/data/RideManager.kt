package io.github.sithengineer.motoqueiro.data

import android.location.LocationManager
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * To start a ride we need to have a GPS connection and optionally a MiBand address.
 */
class RideManager(private val locationListener: GpsStateListener,
    private val locationManager: LocationManager,
    private val rideRepo: RideRepository) {

  private val isGpsActive: Single<Boolean>
    get() = Single.fromCallable { locationListener.isLocationServiceActive(locationManager) }

  /**
   * This [Observable] chain can throw a GpsNotActiveException if GPS is off or in
   * coarse location mode.
   */
  fun start(): Single<Ride> {
    return isGpsActive.flatMap { gpsActive ->
      return@flatMap if (!gpsActive) {
        Single.error<Ride>(GpsNotActiveException())
      } else {
        rideRepo.startRide(generateName())
      }
    }
  }

  private fun generateName(): String =
      DATE_FORMAT.format(Date())

  fun stop(rideId: Long): Completable =
      rideRepo.finishRide(rideId).andThen(rideRepo.sync())

  companion object {
    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
  }
}
