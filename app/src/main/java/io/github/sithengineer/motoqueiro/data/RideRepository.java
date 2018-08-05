package io.github.sithengineer.motoqueiro.data;

import android.support.annotation.NonNull;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.Ride;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.Calendar;
import java.util.UUID;
import rx.Completable;
import rx.Single;
import timber.log.Timber;

public class RideRepository {
  private final RideDataSource localDataSource;
  private final RideDataSource remoteDataSource;
  private final Preferences preferences;

  public RideRepository(RideDataSource localDataSource, RideDataSource remoteDataSource,
      Preferences preferences) {
    this.localDataSource = localDataSource;
    this.remoteDataSource = remoteDataSource;
    this.preferences = preferences;
  }

  public String startRide(@NonNull final String name) {
    String rideId = generateRideId();
    Ride ride = new Ride(rideId, name, Calendar.getInstance().getTimeInMillis());
    localDataSource.saveRide(ride);
    preferences.setRideId(ride.getId());
    return rideId;
  }

  public Single<Boolean> finishRide(String rideId) {
    return localDataSource.markCompleted(rideId);
  }

  private String generateRideId() {
    return UUID.randomUUID().toString();
  }

  public Single<Long> saveHeartRate(String rideId, int heartRate) {
    final HeartRatePoint point =
        new HeartRatePoint(heartRate, Calendar.getInstance().getTimeInMillis());
    return localDataSource.saveHeartRateData(rideId, point);
  }

  public Single<Long> saveGpsCoordinate(String rideId, double lat, double lng) {
    final GpsPoint point = new GpsPoint(lat, lng, Calendar.getInstance().getTimeInMillis());
    return localDataSource.saveGpsData(rideId, point);
  }

  public Single<Long> saveAccelerometerCapture(String rideId, float xx, float yy, float zz) {
    final TriDimenPoint point =
        new TriDimenPoint(xx, yy, zz, Calendar.getInstance().getTimeInMillis());
    return localDataSource.saveAccelerometerData(rideId, point);
  }

  public Single<Long> saveGyroscopeCapture(String rideId, float xx, float yy, float zz) {
    final TriDimenPoint point =
        new TriDimenPoint(xx, yy, zz, Calendar.getInstance().getTimeInMillis());
    return localDataSource.saveGyroscopeData(rideId, point);
  }

  /**
   * Get all the non-sync'ed and finished rides and sends them to the server, for data
   * storage.
   * After sync, mark all the sent ride data as sync'ed
   */
  public Completable sync() {
    return localDataSource.getCompletedRides()
        .toObservable()
        .flatMapIterable(list -> list)
        .filter(ride -> !ride.isSynced())
        .flatMapSingle(ride -> remoteDataSource.saveRide(ride)
            .flatMap(savedId -> localDataSource.markSynced(ride.getId()))
            .doOnError(err -> Timber.e(err)))
        .toList()
        .toCompletable();
  }

  public Single<Long> saveGravityCapture(String rideId, float xx, float yy, float zz) {
    final TriDimenPoint point =
        new TriDimenPoint(xx, yy, zz, Calendar.getInstance().getTimeInMillis());
    return localDataSource.saveGravityData(rideId, point);
  }
}
