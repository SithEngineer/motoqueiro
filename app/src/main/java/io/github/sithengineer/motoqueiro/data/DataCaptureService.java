package io.github.sithengineer.motoqueiro.data;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException;
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Completable;
import rx.Single;
import timber.log.Timber;

public class DataCaptureService extends Service {

  public static final String RIDE_NAME = "ride_name";
  public static final String DEVICE_POSITION = "device_position";
  public static final String STOP_SERVICE = "stop_service";

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

  @Inject DataManager dataManager;
  @Named(DataCaptureModule.SERVICE_SUBSCRIPTIONS) @Inject CompositeSubscriptionManager
      subscriptionManager;
  @Inject GpsStateListener locationListener;
  @Inject LocationManager locationManager;
  @Inject RideRepository rideRepo;

  private boolean isGpsActive() {
    return locationListener.isLocationServiceActive(locationManager);
  }

  /**
   * This method can throw a GpsNotActiveException if GPS is off or in
   * coarse location mode.
   */
  public String start(String rideName) {
    if (!isGpsActive()) {
      throw new GpsNotActiveException();
    }
    String name = TextUtils.isEmpty(rideName) ? generateName() : rideName;
    return rideRepo.startRide(name);
  }

  private String generateName() {
    return DATE_FORMAT.format(new Date());
  }

  public Completable stop(String rideId) {
    return rideRepo.finishRide(rideId).flatMapCompletable(success -> {
      if (success) {
        return rideRepo.sync();
      } else {
        return Completable.complete();
      }
    });
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    final Bundle extras = intent.getExtras();
    startDataCapture(extras);
    return START_STICKY;
  }

  @Override public void onDestroy() {
    subscriptionManager.clearAll();
    subscriptionManager = null;

    super.onDestroy();
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  private void startDataCapture(Bundle extras) {
    // inject dependencies
    MotoqueiroApp.get(this).createDataCaptureComponent().inject(this);

    String rideName = extras.getString(RIDE_NAME);
    if (TextUtils.isEmpty(rideName)) {
      rideName = generateName();
    }

    subscriptionManager.add(Single.just(start(rideName))
        .flatMapObservable(rideId -> dataManager.gatherData().doOnUnsubscribe(() -> stop(rideId)))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }
}
