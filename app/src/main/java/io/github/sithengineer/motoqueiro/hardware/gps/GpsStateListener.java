package io.github.sithengineer.motoqueiro.hardware.gps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import timber.log.Timber;

public class GpsStateListener implements LocationListener {

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
