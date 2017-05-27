package io.github.sithengineer.motoqueiro.data.model;

abstract class TimeStamped {
  private final long timestamp;

  TimeStamped(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
