package io.github.sithengineer.motoqueiro.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import io.github.sithengineer.motoqueiro.PermissionAuthority
import io.github.sithengineer.motoqueiro.PermissionResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.LinkedList

abstract class BaseActivity : AppCompatActivity(),
    PermissionAuthority {
  private var permissionSubject: PublishSubject<PermissionResponse>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    permissionSubject = PublishSubject.create()
  }

  override fun onDestroy() {
    if (permissionSubject != null) {
      permissionSubject!!.onComplete()
      permissionSubject = null
    }
    super.onDestroy()
  }

  // todo improve the permission request system to show a rationale when asking for permissions
  override fun askForPermissions(permissionRequests: Array<String>, requestCode: Int) {
    val permissionsToRequest = LinkedList<String>()

    for (permissionRequest in permissionRequests) {
      if (ContextCompat.checkSelfPermission(this,
              permissionRequest) != PackageManager.PERMISSION_GRANTED) {
        permissionsToRequest.add(permissionRequest)
      } else {
        permissionSubject!!.onNext(
            PermissionResponse(permissionRequest, requestCode,
                true))
      }
    }

    if (!permissionsToRequest.isEmpty()) {
      ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(),
          requestCode)
    }
  }

  /**
   * @return A continuous [Observable] with permission result.
   */
  override fun getPermissionResult(requestCode: Int): Observable<PermissionResponse> {
    return permissionSubject!!.filter { (_, requestCode1) -> requestCode1 == requestCode }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {
    for (i in permissions.indices) {
      permissionSubject!!.onNext(
          PermissionResponse(permissions[i], requestCode,
              grantResults[i] == PackageManager.PERMISSION_GRANTED))
    }
  }
}
