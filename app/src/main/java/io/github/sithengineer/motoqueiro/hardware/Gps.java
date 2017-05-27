package io.github.sithengineer.motoqueiro.hardware;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class Gps implements HardwareObservable<LatLng> {

  private final LocationManager locationManager;

  @Inject public Gps(LocationManager locationManager) {
    this.locationManager = locationManager;
  }

  public Observable<LatLng> mock() {
    return Observable.interval(0, 1, TimeUnit.SECONDS)
        .map(count -> new LatLng(count / 0.3, count / 0.7, System.currentTimeMillis()));
  }

  @SuppressWarnings("MissingPermission") public Observable<LatLng> listen() {
    return Observable.create(subscriber -> {
      final GpsStateListener listener = new GpsStateListener() {
        @Override public void onLocationChanged(Location location) {
          super.onLocationChanged(location);
          if (!subscriber.isUnsubscribed()) {
            final LatLng latLng = new LatLng(location);
            Timber.v("gps: %s",latLng.toString());
            subscriber.onNext(latLng);
          }
        }
      };
      // needs to check permissions on the fly (android 6+)
      subscriber.add(Subscriptions.create(() -> locationManager.removeUpdates(listener)));
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300L, 0.3f, listener);
    });
  }

  public static class GpsStateListener implements LocationListener {

    @Override public void onLocationChanged(Location location) {
      // does nothing
    }

    @Override public void onStatusChanged(String s, int i, Bundle bundle) {
      // does nothing
    }

    @Override public void onProviderEnabled(String s) {
      Timber.i("location provider enabled");
    }

    @Override public void onProviderDisabled(String s) {
      Timber.w("location provider disabled");
    }

    public boolean isLocationServiceActive(LocationManager locationManager) {
      return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
  }
}
