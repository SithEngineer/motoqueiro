package io.github.sithengineer.motoqueiro.data.remote;

import io.github.sithengineer.motoqueiro.data.RideDataSource;
import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.RidePart;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import io.github.sithengineer.motoqueiro.network.RideWebService;
import java.util.List;
import rx.Completable;
import rx.Observable;
import rx.Single;

public class RideRemoteDataSource implements RideDataSource {

  private final RideWebService rideWebService;

  public RideRemoteDataSource(RideWebService rideWebService) {
    this.rideWebService = rideWebService;
  }

  @Override public Completable saveGpsData(String rideId, List<GpsPoint> points) {
    return Completable.complete();
  }

  @Override public Single<Long> saveGpsData(String rideId, GpsPoint coords) {
    return Single.just(0L);
  }

  @Override public Observable<List<GpsPoint>> getGpsData(String rideId) {
    return Observable.empty();
  }

  @Override public Completable saveAccelerometerData(String rideId, List<TriDimenPoint> points) {
    return Completable.complete();
  }

  @Override public Single<Long> saveAccelerometerData(String rideId, TriDimenPoint point) {
    return Single.just(0L);
  }

  @Override public Observable<List<TriDimenPoint>> getAccelerometerData(String rideId) {
    return Observable.empty();
  }

  @Override public Completable saveGravityData(String rideId, List<TriDimenPoint> points) {
    return Completable.complete();
  }

  @Override public Single<Long> saveGravityData(String rideId, TriDimenPoint point) {
    return Single.just(0L);
  }

  @Override public Observable<List<TriDimenPoint>> getGravityData(String rideId) {
    return Observable.empty();
  }

  @Override public Completable saveHeartRateData(String rideId, List<HeartRatePoint> points) {
    return Completable.complete();
  }

  @Override public Single<Long> saveHeartRateData(String rideId, HeartRatePoint point) {
    return Single.just(0L);
  }

  @Override public Observable<List<HeartRatePoint>> getHeartRateData(String rideId) {
    return Observable.empty();
  }

  @Override public Completable saveRide(RidePart ride) {
    return rideWebService.upload(ride);
  }

  @Override public Observable<RidePart> getRide(String rideId) {
    return Observable.empty();
  }

  @Override public Completable markCompleted(String rideId) {
    return Completable.complete();
  }

  @Override public Single<List<RidePart>> getCompletedRides() {
    return Single.just(null);
  }

  @Override public Completable markSynced(String rideId) {
    return Completable.complete();
  }
}
