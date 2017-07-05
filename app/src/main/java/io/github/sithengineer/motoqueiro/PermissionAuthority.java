package io.github.sithengineer.motoqueiro;

import rx.Observable;

public interface PermissionAuthority {
  // todo improve the permission request system to show a rationale when asking for permissions
  void askForPermissions(String[] permissionRequest, int requestCode);

  Observable<PermissionResponse> getPermissionResult(int requestCode);
}
