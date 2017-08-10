package io.github.sithengineer.motoqueiro.exception;

public class GpsNotActiveException extends RuntimeException {
  public GpsNotActiveException() {
    super("GPS not active or in coarse mode.");
  }
}
