package io.github.sithengineer.motoqueiro.data;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import java.lang.ref.WeakReference;

public class DataCaptureService extends IntentService {

  private StopSignalReceiver stopSignalReceiver;

  public DataCaptureService() {
    super("Data capture service");
  }

  @Override public void onDestroy() {
    getApplicationContext().unregisterReceiver(stopSignalReceiver);
    stopSignalReceiver = null;
    super.onDestroy();
  }

  @Override protected void onHandleIntent(@Nullable Intent intent) {
    stopSignalReceiver = new StopSignalReceiver(this);
    getApplicationContext().registerReceiver(stopSignalReceiver, null);

    // start data capture and stop when we get a broadcast

  }

  private static final class StopSignalReceiver extends BroadcastReceiver {

    private final WeakReference<Service> serviceWeakReference;

    private StopSignalReceiver(Service serviceToStop) {
      serviceWeakReference = new WeakReference<>(serviceToStop);
    }

    @Override public void onReceive(Context context, Intent intent) {
      Service s = serviceWeakReference.get();
      if (s != null) {
        s.stopSelf();
      }
    }
  }
}
