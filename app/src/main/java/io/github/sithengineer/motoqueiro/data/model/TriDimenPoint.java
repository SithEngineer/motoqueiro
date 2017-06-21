package io.github.sithengineer.motoqueiro.data.model;

import com.google.gson.annotations.SerializedName;

public class TriDimenPoint extends TimeStamped {
  @SerializedName("x") private final float x;
  @SerializedName("y") private final float y;
  @SerializedName("z") private final float z;

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
