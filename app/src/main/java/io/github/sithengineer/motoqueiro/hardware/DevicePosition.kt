package io.github.sithengineer.motoqueiro.hardware

enum class DevicePosition private constructor(val value: Int) {
  BAR_MOUNT(0), PANTS_POCKET(1), JACKET_POCKET(2), BACKPACK(3), HIP_POUCH(4);

  companion object {

    fun fromValue(value: Int): DevicePosition {
      for (devicePosition in DevicePosition.values()) {
        if (devicePosition.value == value) {
          return devicePosition
        }
      }
      throw IndexOutOfBoundsException(
          String.format("Value %d is not a valid device mount position", value))
    }
  }
}
