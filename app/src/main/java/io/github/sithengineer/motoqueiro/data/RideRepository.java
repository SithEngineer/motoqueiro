package io.github.sithengineer.motoqueiro.data;

import android.support.annotation.NonNull;
import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.RidePart;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.UUID;
import rx.Completable;
import rx.Single;
import timber.log.Timber;

public class RideRepository {
  private final RideDataSource localDataSource;
  // todo use this for remote sync
  private final RideDataSource remoteDataSource;

  public RideRepository(RideDataSource localDataSource, RideDataSource remoteDataSource) {
    this.localDataSource = localDataSource;
    this.remoteDataSource = remoteDataSource;
  }

  public Single<String> startRide(@NonNull final String name) {
    return generateRideId().flatMap(rideId -> {
      RidePart ridePart =
          new RidePart(rideId, name, System.currentTimeMillis(), 0, false);
      return localDataSource.saveRide(ridePart).map(__ -> rideId);
    });
  }

  public Completable finishRide(String rideId) {
    return localDataSource.markCompleted(rideId)
        .toCompletable()
        .andThen(sync())
        .onErrorResumeNext(err -> {
          Timber.e(err);
          return Completable.complete();
        });
  }

  private Single<String> generateRideId() {
    return Single.just(UUID.randomUUID().toString());
  }

  public Completable saveHeartRate(String rideId, int heartRate) {
    return Single.just(new HeartRatePoint(heartRate, System.currentTimeMillis()))
        .flatMapCompletable(
            heartRatePoint -> localDataSource.saveHeartRateData(rideId, heartRatePoint)
                .toCompletable());
  }

  public Completable saveGpsCoordinate(String rideId, double lat, double lng) {
    return Single.just(new GpsPoint(lat, lng, System.currentTimeMillis()))
        .flatMapCompletable(
            gpsPoint -> localDataSource.saveGpsData(rideId, gpsPoint).toCompletable());
  }

  public Completable saveAccelerometerCapture(String rideId, float xx, float yy,
      float zz) {
    return Single.just(new TriDimenPoint(xx, yy, zz, System.currentTimeMillis()))
        .flatMapCompletable(
            triDimenPoint -> localDataSource.saveAccelerometerData(rideId, triDimenPoint)
                .toCompletable());
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
        .flatMapSingle(ride -> remoteDataSource.saveRide(ride)
            .flatMap(__ -> localDataSource.markSynced(ride.getId()))
            .doOnError(err -> Timber.e(err)))
        .toList()
        .toCompletable();
  }
}
