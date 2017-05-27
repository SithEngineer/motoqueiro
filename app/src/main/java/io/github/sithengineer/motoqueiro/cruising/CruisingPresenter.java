package io.github.sithengineer.motoqueiro.cruising;

import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.hardware.MiBand;
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng;
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Inject;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

// TODO
// insert data capture in batches
// use an entity to handle data save / load logic, given a repository
public class CruisingPresenter implements CruisingContract.Presenter {

  private final CruisingContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;
  private final Accelerometer accelerometer;
  private final Gps gps;
  private final MiBand miBand;
  private final RideRepository rideRepo;
  private final String rideId;

  @Inject public CruisingPresenter(CruisingContract.View view, CompositeSubscriptionManager subscriptionManager,
      Accelerometer accelerometer, Gps gps, MiBand miBand, RideRepository rideRepo, String rideId) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.accelerometer = accelerometer;
    this.gps = gps;
    this.miBand = miBand;
    this.rideRepo = rideRepo;
    this.rideId = rideId;
  }

  @Override public void start() {
    // gps data generator observable
    Observable<Void> generateGpsObservable =
        gps.listen().flatMap(pos -> handleGpsPositionCapture(pos).toObservable()).map(__ -> null);

    // accelerometer sensor data generator observable
    Observable<Void> generateAccelObservable =
        accelerometer.listen().flatMap(accel -> handleAccelerometerCapture(accel).toObservable()).map(__ -> null);

    // miband heart rate sensor data generator observable
    //Observable<Void> generateHeartRateObservable =
    //    miBand.listen().flatMap(accel -> handleMiBandCapture(accel)).map(__ -> null);

    Observable<Void> completeRide =
        view.stopClick().flatMap(__ -> stopCruising().toObservable());

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(event -> Observable.merge(generateGpsObservable, generateAccelObservable, completeRide
            /*,generateHeartRateObservable*/))
        .doOnUnsubscribe(() -> Timber.i("Unsubscribed from Cruising Presenter"))
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  @Override public Completable handleMiBandCapture(MiBandData bandData) {
    return rideRepo.saveHeartRate(rideId, bandData.getHeartRateBpm());
  }

  @Override public Completable stopCruising() {
    return rideRepo.finishRide(rideId).doOnCompleted(() -> {
      // the user pressed stop button in this chain of events and at this point we
      // already sync'ed the data with the server. we do not need to keep listening
      // to sensor updates so cleaning all subscriptions (to sensors) will stop them
      stop();

      view.goToStatistics();
    });
  }

  @Override public Completable handleGpsPositionCapture(LatLng capture) {
    return rideRepo.saveGpsCoordinate(rideId, capture.getLat(), capture.getLng());
  }

  @Override public Completable handleAccelerometerCapture(RelativeCoordinates capture) {
    return rideRepo.saveAccelerometerCapture(rideId, capture.getXx(), capture.getYy(), capture.getZz());
  }
}
