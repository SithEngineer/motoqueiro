package io.github.sithengineer.motoqueiro.data.model;

import com.google.gson.annotations.SerializedName;

public class GpsPoint extends TimeStamped {
  @SerializedName("latitude") private final double latitude;
  @SerializedName("longitude") private final double longitude;

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
