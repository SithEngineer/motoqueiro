package io.github.sithengineer.motoqueiro.hardware;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import io.github.sithengineer.motoqueiro.exception.SensorNotAvailableException;
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates;
import javax.inject.Inject;
import rx.Observable;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class Gyroscope implements HardwareObservable<RelativeCoordinates> {

  private final SensorManager sensorManager;

  @Inject public Gyroscope(SensorManager sensorManager) {
    this.sensorManager = sensorManager;
  }

  @Override public Observable<RelativeCoordinates> listen() {
    return Observable.create(subscriber -> {
      if (!subscriber.isUnsubscribed()) {
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroSensor == null) {
          subscriber.onError(new SensorNotAvailableException("Gyroscope not available"));
          return;
        }
        SensorEventListener listener = new SensorEventListener() {
          @Override public void onSensorChanged(SensorEvent sensorEvent) {
            if (!subscriber.isUnsubscribed()) {
              final RelativeCoordinates coordinates =
                  new RelativeCoordinates(sensorEvent.values[0], sensorEvent.values[1],
                      sensorEvent.values[2], sensorEvent.timestamp);
              Timber.v("gyroscope: %s", coordinates.toString());
              subscriber.onNext(coordinates);
            }
          }

          @Override public void onAccuracyChanged(Sensor sensor, int i) {
            // does nothing
          }
        };
        subscriber.add(Subscriptions.create(() -> {
          sensorManager.unregisterListener(listener, gyroSensor);
        }));
        sensorManager.registerListener(listener, gyroSensor,
            SensorManager.SENSOR_DELAY_GAME);
      }
    });
  }
}
