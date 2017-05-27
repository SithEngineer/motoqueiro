package io.github.sithengineer.motoqueiro.hardware.capture;

import android.location.Location;

public class LatLng {

  private final double lat;
  private final double lng;
  private final long timestamp;

  public LatLng(double lat, double lng, long timestamp) {
    this.lat = lat;
    this.lng = lng;
    this.timestamp = timestamp;
  }

  public LatLng(Location location) {
    this.lat = location.getLatitude();
    this.lng = location.getLongitude();
    this.timestamp = location.getTime();
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override public String toString() {
    return "lat=" + lat + ", lng=" + lng + ", timestamp=" + timestamp;
  }
}
