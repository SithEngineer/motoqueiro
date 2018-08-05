package io.github.sithengineer.motoqueiro.data;

import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gravity;
import io.github.sithengineer.motoqueiro.hardware.Gyroscope;
import io.github.sithengineer.motoqueiro.hardware.MiBandService;
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng;
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates;
import io.github.sithengineer.motoqueiro.hardware.gps.Gps;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

public class DataManager {

  private final Accelerometer accelerometer;
  private final Gyroscope gyroscope;
  private final Gravity gravity;
  private final Gps gps;
  private final MiBandService miBand;
  private final RideRepository rideRepo;
  private final Preferences preferences;

  public DataManager(Accelerometer accelerometer, Gyroscope gyroscope, Gravity gravity, Gps gps,
      MiBandService miBand, RideRepository rideRepo, Preferences preferences) {
    this.accelerometer = accelerometer;
    this.gyroscope = gyroscope;
    this.gravity = gravity;
    this.gps = gps;
    this.miBand = miBand;
    this.rideRepo = rideRepo;
    this.preferences = preferences;
  }

  public Observable<Void> gatherData() {
    // gps data generator observable
    Observable<Void> generateGpsObservable =
        gps.listen().flatMapSingle(pos -> handleGpsPositionCapture(pos)).map(__ -> null);

    // accelerometer sensor data generator observable
    Observable<Void> generateAccelObservable = accelerometer.listen()
        .flatMapSingle(accelData -> handleAccelerometerCapture(accelData))
        .onErrorResumeNext(err -> {
          Timber.e(err);
          return Observable.empty();
        })
        .map(__ -> null);

    // gyroscope sensor data generator observable
    Observable<Void> generateGravityObservable = gravity.listen()
        .flatMapSingle(gravityData -> handleGravityCapture(gravityData))
        .onErrorResumeNext(err -> {
          Timber.e(err);
          return Observable.empty();
        })
        .map(__ -> null);

    // gyroscope sensor data generator observable
    Observable<Void> generateGyroObservable = gyroscope.listen()
        .flatMapSingle(gyroData -> handleGyroscopeCapture(gyroData))
        .onErrorResumeNext(err -> {
          Timber.e(err);
          return Observable.empty();
        })
        .map(__ -> null);

    // miband heart rate sensor data generator observable
    Observable<Void> generateHeartRateObservable = miBand.listen()
        .flatMapSingle(heartData -> handleMiBandCapture(heartData))
        .onErrorResumeNext(err -> {
          Timber.e(err);
          return Observable.empty();
        })
        .map(__ -> null);

    return Observable.merge(generateGpsObservable, generateAccelObservable, generateGyroObservable,
        generateGravityObservable, generateHeartRateObservable);
  }

  private String getRideId() {
    return preferences.getRideId();
  }

  private Single<Long> handleMiBandCapture(MiBandData bandData) {
    return rideRepo.saveHeartRate(getRideId(), bandData.getHeartRateBpm());
  }

  private Single<Long> handleGpsPositionCapture(LatLng capture) {
    return rideRepo.saveGpsCoordinate(getRideId(), capture.getLat(), capture.getLng());
  }

  private Single<Long> handleAccelerometerCapture(RelativeCoordinates capture) {
    return rideRepo.saveAccelerometerCapture(getRideId(), capture.getXx(), capture.getYy(),
        capture.getZz());
  }

  private Single<Long> handleGravityCapture(RelativeCoordinates capture) {
    return rideRepo.saveGravityCapture(getRideId(), capture.getXx(), capture.getYy(),
        capture.getZz());
  }

  private Single<Long> handleGyroscopeCapture(RelativeCoordinates capture) {
    return rideRepo.saveGyroscopeCapture(getRideId(), capture.getXx(), capture.getYy(),
        capture.getZz());
  }
}
