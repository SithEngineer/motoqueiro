package io.github.sithengineer.motoqueiro.home;

import android.location.LocationManager;
import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import java.util.concurrent.TimeUnit;
import rx.Completable;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

public class HomePresenter implements HomeContract.Presenter {

  private final CompositeSubscriptionManager subscriptionManager;
  private final HomeContract.View view;
  private final LocationManager locationManager;
  private final Gps.GpsStateListener locationListener;
  private final RideRepository rideRepo;

  public HomePresenter(HomeContract.View view, CompositeSubscriptionManager subscriptionManager, LocationManager locationManager,
      Gps.GpsStateListener locationListener, RideRepository rideRepo) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.locationManager = locationManager;
    this.locationListener = locationListener;
    this.rideRepo = rideRepo;
  }

  @Override public void start() {
    Timber.d("start called");
    Observable<Void> checkGps = checkGpsConnectivity().flatMapObservable(gpsActive -> {
      if (gpsActive) {
        return view.handleStartClick()
            .flatMap(__ -> rideRepo.startRide().doOnSuccess(rideId -> goToCruisingActivity(rideId)).toObservable())
            .map(__ -> null);
      }

      return Completable.fromAction(() -> showActivateGpsViewMessage())
          .andThen(Completable.timer(2, TimeUnit.SECONDS).doOnCompleted(() -> showActivateGpsView()))
          .toObservable();
    });

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.RESUME)
        .flatMap(__ -> checkGps)
        .compose(view.bindUntilEvent(FragmentEvent.PAUSE))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  @Override public void showActivateGpsViewMessage() {
    view.showActivateGpsViewMessage();
  }

  @Override public void showActivateGpsView() {
    view.sendToGpsSettings();
  }

  @Override public Single<Boolean> checkGpsConnectivity() {
    return Single.fromCallable(() -> locationListener.isLocationServiceActive(locationManager));
  }

  @Override public void goToCruisingActivity(String rideId) {
    view.goToCruisingActivity(rideId);
  }

  private void addDummyAccountForSyncAdapter() {
    // TODO: 14/4/2017 sithengineer
  }
}
