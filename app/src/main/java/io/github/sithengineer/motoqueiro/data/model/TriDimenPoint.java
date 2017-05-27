package io.github.sithengineer.motoqueiro.data.model;

public class TriDimenPoint extends TimeStamped {

  private final float x;
  private final float y;
  private final float z;

  public TriDimenPoint(float x, float y, float z, long timestamp) {
    super(timestamp);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }
}
