package io.github.sithengineer.motoqueiro;

public class PermissionResponse {
  private final String permissionRequest;
  private final int requestCode;
  private final boolean granted;

  public PermissionResponse(String permissionRequest, int requestCode, boolean granted) {
    this.permissionRequest = permissionRequest;
    this.requestCode = requestCode;
    this.granted = granted;
  }

  public boolean isGranted() {
    return granted;
  }

  public String getPermissionRequest() {
    return permissionRequest;
  }

  public int getRequestCode() {
    return requestCode;
  }
}
