package io.github.sithengineer.motoqueiro.data.model;

import com.google.gson.annotations.SerializedName;

abstract class TimeStamped {
  @SerializedName("timestamp") private final long timestamp;

  TimeStamped(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
