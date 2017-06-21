package io.github.sithengineer.motoqueiro.data.model;

import com.google.gson.annotations.SerializedName;

public class HeartRatePoint extends TimeStamped {
  @SerializedName("beatsPerMinute") private final int heartRate;

  public HeartRatePoint(int heartRate, long timestamp) {
    super(timestamp);
    this.heartRate = heartRate;
  }

  public int getHeartRate() {
    return heartRate;
  }
}
