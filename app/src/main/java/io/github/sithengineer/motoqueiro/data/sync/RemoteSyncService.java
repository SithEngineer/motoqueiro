package io.github.sithengineer.motoqueiro.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class RemoteSyncService extends Service {

  private static final Object _lock = new Object();

  private static RemoteSyncAdapter adapter = null;

  @Override public void onCreate() {
    super.onCreate();
    synchronized (_lock) {
      if (adapter == null) {
        adapter = new RemoteSyncAdapter(getApplicationContext(), true, false);
      }
    }
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return adapter.getSyncAdapterBinder();
  }
}
