package io.github.sithengineer.motoqueiro.cruising;

import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.hardware.MiBand;
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng;
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates;
import rx.Completable;
import rx.Observable;

public class DataManager {

  private final Accelerometer accelerometer;
  private final Gps gps;
  private final MiBand miBand;
  private final RideRepository rideRepo;
  private final String rideId;

  public DataManager(Accelerometer accelerometer, Gps gps, MiBand miBand,
      RideRepository rideRepo, String rideId) {
    this.accelerometer = accelerometer;
    this.gps = gps;
    this.miBand = miBand;
    this.rideRepo = rideRepo;
    this.rideId = rideId;
  }

  public Observable<Void> gatherData() {
    // gps data generator observable
    Observable<Void> generateGpsObservable = gps.listen()
        .flatMap(pos -> handleGpsPositionCapture(pos).toObservable())
        .map(__ -> null);

    // accelerometer sensor data generator observable
    Observable<Void> generateAccelObservable = accelerometer.listen()
        .flatMap(accel -> handleAccelerometerCapture(accel).toObservable())
        .map(__ -> null);

    // miband heart rate sensor data generator observable
    Observable<Void> generateHeartRateObservable = miBand.listen()
        .flatMap(accel -> handleMiBandCapture(accel).toObservable())
        .map(__ -> null);

    return Observable.merge(generateGpsObservable, generateAccelObservable,
        generateHeartRateObservable);
  }

  private Completable handleMiBandCapture(MiBandData bandData) {
    return rideRepo.saveHeartRate(rideId, bandData.getHeartRateBpm());
  }

  private Completable handleGpsPositionCapture(LatLng capture) {
    return rideRepo.saveGpsCoordinate(rideId, capture.getLat(), capture.getLng());
  }

  private Completable handleAccelerometerCapture(RelativeCoordinates capture) {
    return rideRepo.saveAccelerometerCapture(rideId, capture.getXx(), capture.getYy(),
        capture.getZz());
  }
}
