package io.github.sithengineer.motoqueiro.hardware

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.github.sithengineer.motoqueiro.exception.SensorNotAvailableException
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates
import io.reactivex.Observable
import org.threeten.bp.Instant
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Accelerometer @Inject constructor(private val sensorManager: SensorManager) :
    HardwareObservable<RelativeCoordinates> {

  fun mock(): Observable<RelativeCoordinates> {
    return Observable.interval(0, 1, TimeUnit.SECONDS)
        .map { count ->
          RelativeCoordinates(count / 0.1f, count / 0.3f, count / 0.6f,
              Instant.now().toEpochMilli())
        }
  }

  override fun listen(): Observable<RelativeCoordinates> {
    return Observable.create { subscriber ->
      if (!subscriber.isDisposed) {
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelSensor == null) {
          subscriber.onError(
              SensorNotAvailableException("Accelerometer not available"))
        }
        val listener = object : SensorEventListener {
          override fun onSensorChanged(sensorEvent: SensorEvent) {
            if (!subscriber.isDisposed) {
              val coordinates = RelativeCoordinates(sensorEvent.values[0], sensorEvent.values[1],
                  sensorEvent.values[2], sensorEvent.timestamp)
              Timber.v("accelerometer: %s", coordinates.toString())
              subscriber.onNext(coordinates)
            }
          }

          override fun onAccuracyChanged(sensor: Sensor, i: Int) {
            // does nothing
          }
        }
        subscriber.setCancellable { sensorManager.unregisterListener(listener, accelSensor) }
        sensorManager.registerListener(listener, accelSensor,
            SensorManager.SENSOR_DELAY_GAME)
      }
    }
  }
}
