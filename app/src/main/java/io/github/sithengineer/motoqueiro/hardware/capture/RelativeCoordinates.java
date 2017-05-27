package io.github.sithengineer.motoqueiro.hardware.capture;

public class RelativeCoordinates {
  private final float xx;
  private final float yy;
  private final float zz;
  private final long timestamp;

  public RelativeCoordinates(float xx, float yy, float zz, long timestamp) {
    this.xx = xx;
    this.yy = yy;
    this.zz = zz;
    this.timestamp = timestamp;
  }

  public float getXx() {
    return xx;
  }

  public float getYy() {
    return yy;
  }

  public float getZz() {
    return zz;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override public String toString() {
    return "x=" + xx + ", y=" + yy + ", z=" + zz + ", timestamp=" + timestamp;
  }
}
