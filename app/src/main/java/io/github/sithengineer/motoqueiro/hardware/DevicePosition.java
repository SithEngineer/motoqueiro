package io.github.sithengineer.motoqueiro.hardware;

public enum DevicePosition {
  BAR_MOUNT(0), PANTS_POCKET(1), JACKET_POCKET(2), BACKPACK(3), HIP_POUCH(4);

  private final int value;

  DevicePosition(int value) {
    this.value = value;
  }

  public static DevicePosition fromValue(int value) {
    for (final DevicePosition devicePosition : DevicePosition.values()) {
      if (devicePosition.getValue() == value) {
        return devicePosition;
      }
    }
    throw new IndexOutOfBoundsException(
        String.format("Value %d is not a valid device mount position", value));
  }

  public int getValue() {
    return value;
  }
}
