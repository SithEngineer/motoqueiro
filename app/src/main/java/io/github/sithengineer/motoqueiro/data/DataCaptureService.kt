package io.github.sithengineer.motoqueiro.data

import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import io.github.sithengineer.motoqueiro.MotoqueiroApp
import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DataCaptureService : Service() {

  private val subscriptionManager = CompositeSubscriptionManager()

  @Inject
  lateinit var dataManager: DataManager

  @Inject
  lateinit var locationListener: GpsStateListener

  @Inject
  lateinit var locationManager: LocationManager

  @Inject
  lateinit var rideRepo: RideRepository

  private val isGpsActive: Boolean
    get() = locationListener.isLocationServiceActive(locationManager)

  /**
   * This method can throw a GpsNotActiveException if GPS is off or in
   * coarse location mode.
   */
  fun start(rideName: String): Single<Ride> {
    if (!isGpsActive) {
      throw GpsNotActiveException()
    }
    val name = if (TextUtils.isEmpty(rideName)) generateName() else rideName
    return rideRepo.startRide(name)
  }

  private fun generateName(): String {
    return DATE_FORMAT.format(Date())
  }

  fun stop(rideId: Long): Completable {
    return rideRepo.finishRide(rideId).andThen(rideRepo.sync())
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val extras = intent.extras
    startDataCapture(extras!!)
    return Service.START_STICKY
  }

  override fun onDestroy() {
    subscriptionManager.clearAll()
    super.onDestroy()
  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  private fun startDataCapture(extras: Bundle) {
    // inject dependencies
    MotoqueiroApp[this].createDataCaptureComponent().inject(this)

    var rideName = extras.getString(RIDE_NAME)
    if (TextUtils.isEmpty(rideName)) {
      rideName = generateName()
    }

    subscriptionManager.add(
        start(rideName)
            .flatMapCompletable { ride ->
              dataManager.gatherData().doOnTerminate {
                stop(ride.id)
              }
            }
            .subscribeBy(
                onComplete = { },
                onError = { err -> Timber.e(err) }
            )
    )
  }

  companion object {
    const val RIDE_NAME = "ride_name"
    const val DEVICE_POSITION = "device_position"
    const val STOP_SERVICE = "stop_service"
    // TODO migrate to threetenabp
    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
  }
}
