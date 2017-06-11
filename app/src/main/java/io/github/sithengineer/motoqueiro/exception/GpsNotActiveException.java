package io.github.sithengineer.motoqueiro.exception;

public class GpsNotActiveException extends Throwable {
  public GpsNotActiveException() {
    super("GPS not active or in coarse mode.");
  }
}
