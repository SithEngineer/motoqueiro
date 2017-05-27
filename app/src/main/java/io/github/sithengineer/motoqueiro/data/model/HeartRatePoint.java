package io.github.sithengineer.motoqueiro.data.model;

public class HeartRatePoint extends TimeStamped {
  private final int beatsPerMinute;

  public HeartRatePoint(int heartRate, long timestamp) {
    super(timestamp);
    this.beatsPerMinute = heartRate;
  }

  public int getBeatsPerMinute() {
    return beatsPerMinute;
  }
}
