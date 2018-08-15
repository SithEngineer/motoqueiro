package io.github.sithengineer.motoqueiro.hardware

import java.util.UUID

// based in constants from https://github.com/yonixw/mi-band-2
object MiBandConstants {
  //this is common for all BTLE devices. see http://stackoverflow.com/questions/18699251/finding-out-android-bluetooth-le-gatt-profiles
  private const val BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb"

  val UUID_SERVICE_GENERIC = UUID.fromString(String.format(BASE_UUID, "1800"))!!

  val UUID_SERVICE_MIBAND_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE0"))!!

  val UUID_SERVICE_MIBAND2_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE1"))!!

  val UUID_SERVICE_HEARTBEAT = UUID.fromString(String.format(BASE_UUID, "180D"))!!

  // General service
  val UUID_CHARACTERISTIC_DEVICE_NAME = UUID.fromString(String.format(BASE_UUID, "2A00"))!!

  // Miband service 1
  val UUID_BUTTON_TOUCH = UUID.fromString("00000010-0000-3512-2118-0009af100700")!!

  //Heart beat service
  val UUID_START_HEARTRATE_CONTROL_POINT = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb")!!

  val UUID_NOTIFICATION_HEARTRATE = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")!!

  val BYTE_LAST_HEART_RATE_SCAN = byteArrayOf(21, 1, 1)

  val BYTE_NEW_HEART_RATE_SCAN = byteArrayOf(21, 2, 1)

  val UUID_DESCRIPTOR_UPDATE_NOTIFICATION = UUID.fromString(
      "00002902-0000-1000-8000-00805f9b34fb")!!
}
