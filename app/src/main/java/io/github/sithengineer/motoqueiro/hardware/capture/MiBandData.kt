package io.github.sithengineer.motoqueiro.hardware.capture

class MiBandData {

  var heartRateBpm: Int = 0
  var isButtonPress: Boolean = false
    private set

  constructor(heartRate: Int) {
    this.isButtonPress = false
    this.heartRateBpm = heartRate
  }

  constructor(buttonPress: Boolean) {
    this.isButtonPress = buttonPress
  }

  constructor()

  /**
   * @return true if heart rate is over 25bpm
   */
  fun hasHeartRate(): Boolean {
    return heartRateBpm > 25
  }

  override fun toString(): String {
    return "heartRate=$heartRateBpm, buttonPress=$isButtonPress"
  }
}
