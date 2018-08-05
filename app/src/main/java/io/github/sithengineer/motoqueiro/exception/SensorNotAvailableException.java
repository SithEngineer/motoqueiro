package io.github.sithengineer.motoqueiro.exception;

public class SensorNotAvailableException extends Throwable {

  public SensorNotAvailableException(String sensorType) {
    super(String.format("Sensor %s not available", sensorType));
  }
}
