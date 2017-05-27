package io.github.sithengineer.motoqueiro.data.model;

public class GpsPoint extends TimeStamped {
  private final double latitude;
  private final double longitude;

  public GpsPoint(double latitude, double longitude, long timestamp) {
    super(timestamp);
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
