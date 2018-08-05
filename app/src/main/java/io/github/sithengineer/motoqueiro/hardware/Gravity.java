package io.github.sithengineer.motoqueiro.hardware;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import io.github.sithengineer.motoqueiro.exception.SensorNotAvailableException;
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class Gravity implements HardwareObservable<RelativeCoordinates> {

  private final SensorManager sensorManager;

  @Inject public Gravity(SensorManager sensorManager) {
    this.sensorManager = sensorManager;
  }

  public Observable<RelativeCoordinates> mock() {
    return Observable.interval(0, 1, TimeUnit.SECONDS)
        .map(count -> new RelativeCoordinates(count / 0.1f, count / 0.3f, count / 0.6f,
            Calendar.getInstance().getTimeInMillis()));
  }

  @Override public Observable<RelativeCoordinates> listen() {
    return Observable.create(subscriber -> {
      if (!subscriber.isUnsubscribed()) {
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (gravitySensor == null) {
          subscriber.onError(new SensorNotAvailableException("Gyroscope not available"));
          return;
        }
        SensorEventListener listener = new SensorEventListener() {
          @Override public void onSensorChanged(SensorEvent sensorEvent) {
            if (!subscriber.isUnsubscribed()) {
              final RelativeCoordinates coordinates =
                  new RelativeCoordinates(sensorEvent.values[0], sensorEvent.values[1],
                      sensorEvent.values[2], sensorEvent.timestamp);
              Timber.v("gravity: %s", coordinates.toString());
              subscriber.onNext(coordinates);
            }
          }

          @Override public void onAccuracyChanged(Sensor sensor, int i) {
            // does nothing
          }
        };
        subscriber.add(Subscriptions.create(() -> {
          sensorManager.unregisterListener(listener, gravitySensor);
        }));
        sensorManager.registerListener(listener, gravitySensor,
            SensorManager.SENSOR_DELAY_GAME);
      }
    });
  }
}
