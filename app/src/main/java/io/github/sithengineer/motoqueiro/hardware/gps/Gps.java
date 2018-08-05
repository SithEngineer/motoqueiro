package io.github.sithengineer.motoqueiro.hardware.gps;

import android.location.Location;
import android.location.LocationManager;
import io.github.sithengineer.motoqueiro.hardware.HardwareObservable;
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class Gps implements HardwareObservable<LatLng> {

  public static final long MIN_TIME = 200L;
  public static final float MIN_DISTANCE = 0.2f;
  private final LocationManager locationManager;

  @Inject public Gps(LocationManager locationManager) {
    this.locationManager = locationManager;
  }

  public Observable<LatLng> mock() {
    return Observable.interval(0, MIN_TIME, TimeUnit.MILLISECONDS)
        .map(count -> new LatLng(count / 0.3, count / 0.7,
            Calendar.getInstance().getTimeInMillis()));
  }

  @SuppressWarnings("MissingPermission") public Observable<LatLng> listen() {
    return Observable.create(subscriber -> {
      final GpsStateListener listener = new GpsStateListener() {
        @Override public void onLocationChanged(Location location) {
          super.onLocationChanged(location);
          if (!subscriber.isUnsubscribed()) {
            final LatLng latLng = new LatLng(location);
            Timber.v("gps: %s", latLng.toString());
            subscriber.onNext(latLng);
          }
        }
      };
      // needs to check permissions on the fly (android 6+)
      subscriber.add(Subscriptions.create(() -> locationManager.removeUpdates(listener)));
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,
          MIN_DISTANCE, listener);
    });
  }
}
