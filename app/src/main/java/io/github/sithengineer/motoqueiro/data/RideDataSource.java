package io.github.sithengineer.motoqueiro.data;

import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.Ride;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.List;
import rx.Completable;
import rx.Single;

public interface RideDataSource {
  Completable saveGpsData(String rideId, List<GpsPoint> points);

  Single<Long> saveGpsData(String rideId, GpsPoint coords);

  Single<List<GpsPoint>> getGpsData(String rideId);

  Completable saveAccelerometerData(String rideId, List<TriDimenPoint> points);

  Single<Long> saveAccelerometerData(String rideId, TriDimenPoint point);

  Single<List<TriDimenPoint>> getAccelerometerData(String rideId);

  Completable saveGyroscopeData(String rideId, List<TriDimenPoint> points);

  Single<Long> saveGyroscopeData(String rideId, TriDimenPoint point);

  Completable saveGravityData(String rideId, List<TriDimenPoint> points);

  Single<Long> saveGravityData(String rideId, TriDimenPoint point);

  Single<List<TriDimenPoint>> getGravityData(String rideId);

  Completable saveHeartRateData(String rideId, List<HeartRatePoint> points);

  Single<Long> saveHeartRateData(String rideId, HeartRatePoint point);

  Single<List<HeartRatePoint>> getHeartRateData(String rideId);

  Single<Long> saveRide(Ride ride);

  Single<List<TriDimenPoint>> getGyroscopeData(String rideId);

  Single<Ride> getRide(String rideId);

  Single<Boolean> markCompleted(String rideId);

  Single<List<Ride>> getCompletedRides();

  Single<Boolean> markSynced(String rideId);
}
