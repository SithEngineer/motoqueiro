package io.github.sithengineer.motoqueiro;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import java.util.LinkedList;
import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class BaseActivity extends AppCompatActivity implements PermissionAuthority {
  private PublishSubject<PermissionResponse> permissionSubject;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    permissionSubject = PublishSubject.create();
  }

  @Override protected void onDestroy() {
    if(permissionSubject!=null) {
      permissionSubject.onCompleted();
      permissionSubject = null;
    }
    super.onDestroy();
  }

  // todo improve the permission request system to show a rationale when asking for permissions
  @Override public void askForPermissions(String[] permissionRequests, int requestCode) {
    LinkedList<String> permissionsToRequest = new LinkedList<>();

    for (final String permissionRequest : permissionRequests) {
      if (ContextCompat.checkSelfPermission(this, permissionRequest)
          != PackageManager.PERMISSION_GRANTED) {
        permissionsToRequest.add(permissionRequest);
      } else {
        permissionSubject.onNext(new PermissionResponse(permissionRequest, requestCode, true));
      }
    }

    if (!permissionsToRequest.isEmpty()) {
      ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]),
          requestCode);
    }
  }

  /**
   * @return A continuous {@link Observable} with permission result.
   */
  @Override public Observable<PermissionResponse> getPermissionResult(int requestCode) {
    return permissionSubject.filter(response -> response.getRequestCode() == requestCode)
        .asObservable();
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    for (int i = 0; i < permissions.length; ++i) {
      permissionSubject.onNext(new PermissionResponse(permissions[i], requestCode,
          grantResults[i] == PackageManager.PERMISSION_GRANTED));
    }
  }
}
