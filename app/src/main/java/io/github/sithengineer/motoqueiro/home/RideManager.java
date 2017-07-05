package io.github.sithengineer.motoqueiro.home;

import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException;
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * To start a ride we need to have a GPS connection and optionally a MiBand address.
 */
public class RideManager {

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
  private final GpsStateListener locationListener;
  private final LocationManager locationManager;
  private final RideRepository rideRepo;

  public RideManager(GpsStateListener locationListener, LocationManager locationManager,
      RideRepository rideRepo) {
    this.locationListener = locationListener;
    this.locationManager = locationManager;
    this.rideRepo = rideRepo;
  }

  /**
   * This {@link Observable} chain can throw a GpsNotActiveException if GPS is off or in
   * coarse
   * location mode.
   */
  public Single<String> start(@Nullable final String name) {
    return isGpsActive().flatMap(gpsActive -> {
      if (!gpsActive) {
        return Single.error(new GpsNotActiveException());
      }
      final String rideName;
      if (TextUtils.isEmpty(name)) {
        rideName = generateName();
      } else {
        rideName = name;
      }

      return rideRepo.startRide(rideName);
    });
  }

  public String generateName() {
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

  private Single<Boolean> isGpsActive() {
    return Single.fromCallable(
        () -> locationListener.isLocationServiceActive(locationManager));
  }
}
