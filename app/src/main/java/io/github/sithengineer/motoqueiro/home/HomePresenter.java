package io.github.sithengineer.motoqueiro.home;

import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

public class HomePresenter implements HomeContract.Presenter {

  private final CompositeSubscriptionManager subscriptionManager;
  private final HomeContract.View view;
  private final RideManager rideManager;
  private final Preferences preferences;
  private final Pattern macValidator;

  public HomePresenter(HomeContract.View view,
      CompositeSubscriptionManager subscriptionManager, RideManager rideManager,
      Preferences preferences) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.rideManager = rideManager;
    this.preferences = preferences;
    macValidator = Pattern.compile(
        "(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}.){2}[0-9A-Fa-f]{4})");
  }

  @Override public void start() {
    Observable<Void> handleStartClick = view.handleStartClick()
        .flatMap(
            __ -> view.getMiBandAddressChanges().first().toSingle().flatMap(address -> {
              // fixme un-comment bluetooth address validation before continuing
              //if (isValidBluetoothAddress(address)) {
              return rideManager.start(view.getRideName()).doOnSuccess(rideId -> {
                goToCruisingActivity(rideId);
              });
              //}
              //return Single.just(address);
            }).toObservable())
        .onErrorResumeNext(err -> handleStartRideError(err).map(__ -> null))
        .map(__ -> null);

    Observable<Void> watchMiBandAddressChange =
        view.getMiBandAddressChanges().doOnNext(address -> {
          if (isValidBluetoothAddress(address)) {
            view.cleanMiBandAddressError();
            saveMiBandAddress(address);
          } else {
            view.showMiBandAddressError();
          }
        }).retry().map(__ -> null);

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> Observable.merge(handleStartClick, watchMiBandAddressChange,
            showMiBandAddress().toObservable()))
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  private boolean isValidBluetoothAddress(String address) {
    return macValidator.matcher(address).matches();
  }

  private void saveMiBandAddress(String address) {
    preferences.setMiBandAddress(address);
  }

  private Completable showMiBandAddress() {
    return Completable.fromAction(
        () -> view.showMiBandAddress(preferences.getMiBandAddressOrDefault()));
  }

  private Observable<Void> handleStartRideError(Throwable err) {
    if (err instanceof GpsNotActiveException) {
      return sendToActivateGpsSettings();
    }

    Timber.e(err);
    view.showGenericError();
    return Observable.empty();
  }

  private Observable<Void> sendToActivateGpsSettings() {
    return Completable.fromAction(() -> showActivateGpsViewMessage())
        .andThen(Completable.timer(2, TimeUnit.SECONDS)
            .doOnCompleted(() -> showActivateGpsView()))
        .toObservable();
  }

  @Override public void showActivateGpsViewMessage() {
    view.showActivateGpsViewMessage();
  }

  @Override public void showActivateGpsView() {
    view.sendToGpsSettings();
  }

  private void goToCruisingActivity(String rideId) {
    view.goToCruisingActivity(rideId);
  }
}
