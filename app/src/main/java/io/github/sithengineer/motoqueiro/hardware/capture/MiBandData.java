package io.github.sithengineer.motoqueiro.hardware.capture;

public class MiBandData {

  private int heartRate;
  private boolean buttonPress;

  public MiBandData(int heartRate) {
    this.buttonPress = false;
    this.heartRate = heartRate;
  }

  public MiBandData(boolean buttonPress) {
    this.buttonPress = buttonPress;
  }

  public MiBandData() {
  }

  public boolean isButtonPress() {
    return buttonPress;
  }

  /**
   * @return true if heart rate is over 25bpm
   */
  public boolean hasHeartRate() {
    return heartRate > 25;
  }

  public int getHeartRateBpm() {
    return heartRate;
  }

  @Override public String toString() {
    return "heartRate=" + heartRate + ", buttonPress=" + buttonPress;
  }
}
