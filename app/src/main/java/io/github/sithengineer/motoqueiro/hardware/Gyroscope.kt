package io.github.sithengineer.motoqueiro.hardware

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.github.sithengineer.motoqueiro.exception.SensorNotAvailableException
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class Gyroscope @Inject constructor(private val sensorManager: SensorManager) :
    HardwareObservable<RelativeCoordinates> {

  override fun listen(): Observable<RelativeCoordinates> {
    return Observable.create { subscriber ->
      if (!subscriber.isDisposed) {

        val gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (gyroSensor == null) {
          subscriber.onError(SensorNotAvailableException("Gyroscope not available"))
        }

        val listener = object : SensorEventListener {
          override fun onSensorChanged(sensorEvent: SensorEvent) {
            if (!subscriber.isDisposed) {
              val coordinates = RelativeCoordinates(sensorEvent.values[0], sensorEvent.values[1],
                  sensorEvent.values[2], sensorEvent.timestamp)
              Timber.v("gyroscope: %s", coordinates.toString())
              subscriber.onNext(coordinates)
            }
          }

          override fun onAccuracyChanged(sensor: Sensor, i: Int) {
            // does nothing
          }
        }

        subscriber.setCancellable { sensorManager.unregisterListener(listener, gyroSensor) }

        sensorManager.registerListener(listener, gyroSensor,
            SensorManager.SENSOR_DELAY_GAME)
      }
    }
  }
}
