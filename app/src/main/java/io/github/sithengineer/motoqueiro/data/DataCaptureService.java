package io.github.sithengineer.motoqueiro.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import rx.Observable;
import rx.Single;
import rx.subjects.PublishSubject;

public class DataCaptureService extends Service {
  public static final String RIDE_NAME = "ride_name";
  public static final String DEVICE_POSITION = "device_position";
  public static final String STOP_SERVICE = "stop_service";
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

  @Inject DataManager dataManager;
  @Inject StopSignalReceiver stopSignalReceiver;
  @Named(DataCaptureModule.SERVICE_SUBSCRIPTIONS) @Inject CompositeSubscriptionManager
      subscriptionManager;
  @Inject GpsStateListener locationListener;
  @Inject LocationManager locationManager;
  @Inject RideRepository rideRepo;

  private Single<Boolean> isGpsActive() {
    return Single.fromCallable(() -> locationListener.isLocationServiceActive(locationManager));
  }

  /**
   * This {@link Observable} chain can throw a GpsNotActiveException if GPS is off or in
   * coarse location mode.
   */
  public Single<String> start() {
    return isGpsActive().flatMap(gpsActive -> {
      if (!gpsActive) {
        return Single.error(new GpsNotActiveException());
      }
      return rideRepo.startRide(generateName());
    });
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
    startDataCapture(intent.getExtras());
    return START_STICKY;
  }

  @Override public void onDestroy() {
    subscriptionManager.clearAll();
    subscriptionManager = null;

    getApplicationContext().unregisterReceiver(stopSignalReceiver);
    stopSignalReceiver = null;

    super.onDestroy();
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  private void startDataCapture(Bundle extras) {
    // inject dependencies
    MotoqueiroApp.get(this).createDataCaptureComponent().inject(this);

    getApplicationContext().registerReceiver(stopSignalReceiver, stopSignalReceiver.getFilter());

    String rideName = extras.getString(RIDE_NAME);
    if (TextUtils.isEmpty(rideName)) {
      rideName = generateName();
    }

    subscriptionManager.add(rideRepo.startRide(rideName)
        .flatMapObservable(rideId -> dataManager.gatherData()
            .flatMap(__ -> stopSignalReceiver.onStopEvent())
            .flatMapCompletable(__ -> stop(rideId))
            .doOnCompleted(() -> stopSelf()))
            //.subscribeOn(Schedulers.io())
        .subscribe());
  }

  public static final class StopSignalReceiver extends BroadcastReceiver {

    private final PublishSubject<Void> stopEventPublisher;

    public StopSignalReceiver() {
      stopEventPublisher = PublishSubject.create();
    }

    public Observable<Void> onStopEvent() {
      return stopEventPublisher;
    }

    @Override public void onReceive(Context context, Intent intent) {
      stopEventPublisher.onNext(null);
    }

    public IntentFilter getFilter() {
      IntentFilter filter = new IntentFilter();
      filter.addAction(STOP_SERVICE);
      return filter;
    }
  }
}
