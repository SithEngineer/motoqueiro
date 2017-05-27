package io.github.sithengineer.motoqueiro.data;

import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.RidePart;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.List;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface RideDataSource {
  Completable saveGpsData(String rideId, List<GpsPoint> points);

  Single<Long> saveGpsData(String rideId, GpsPoint coords);

  Observable<List<GpsPoint>> getGpsData(String rideId);

  Completable saveAccelerometerData(String rideId, List<TriDimenPoint> points);

  Single<Long> saveAccelerometerData(String rideId, TriDimenPoint point);

  Observable<List<TriDimenPoint>> getAccelerometerData(String rideId);

  Completable saveGravityData(String rideId, List<TriDimenPoint> points);

  Single<Long> saveGravityData(String rideId, TriDimenPoint point);

  Observable<List<TriDimenPoint>> getGravityData(String rideId);

  Completable saveHeartRateData(String rideId, List<HeartRatePoint> points);

  Single<Long> saveHeartRateData(String rideId, HeartRatePoint point);

  Observable<List<HeartRatePoint>> getHeartRateData(String rideId);

  Completable saveRide(RidePart ride);

  Observable<RidePart> getRide(String rideId);

  Completable markCompleted(String rideId);

  Single<List<RidePart>> getCompletedRides();

  Completable markSynced(String rideId);
}
