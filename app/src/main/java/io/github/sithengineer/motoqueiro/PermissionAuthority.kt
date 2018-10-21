package io.github.sithengineer.motoqueiro

import io.reactivex.Observable

interface PermissionAuthority {
  // todo improve the permission request system to show a rationale when asking for permissions
  fun askForPermissions(permissionRequests: Array<String>, requestCode: Int)

  fun getPermissionResult(requestCode: Int): Observable<PermissionResponse>
}
