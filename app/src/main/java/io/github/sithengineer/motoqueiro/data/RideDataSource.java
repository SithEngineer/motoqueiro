package io.github.sithengineer.motoqueiro.data;

import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.Ride;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.List;

public interface RideDataSource {
  void saveGpsData(String rideId, List<GpsPoint> points);

  long saveGpsData(String rideId, GpsPoint coords);

  List<GpsPoint> getGpsData(String rideId);

  void saveAccelerometerData(String rideId, List<TriDimenPoint> points);

  long saveAccelerometerData(String rideId, TriDimenPoint point);

  List<TriDimenPoint> getAccelerometerData(String rideId);

  void saveGyroscopeData(String rideId, List<TriDimenPoint> points);

  long saveGyroscopeData(String rideId, TriDimenPoint point);

  void saveGravityData(String rideId, List<TriDimenPoint> points);

  long saveGravityData(String rideId, TriDimenPoint point);

  List<TriDimenPoint> getGravityData(String rideId);

  void saveHeartRateData(String rideId, List<HeartRatePoint> points);

  long saveHeartRateData(String rideId, HeartRatePoint point);

  List<HeartRatePoint> getHeartRateData(String rideId);

  long saveRide(Ride ride);

  List<TriDimenPoint> getGyroscopeData(String rideId);

  Ride getRide(String rideId);

  boolean markCompleted(String rideId);

  List<Ride> getCompletedRides();

  boolean markSynced(String rideId);
}
