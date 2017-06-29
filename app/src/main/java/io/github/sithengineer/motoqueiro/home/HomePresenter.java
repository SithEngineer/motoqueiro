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
  private final HomeNavigator homeNavigator;

  public HomePresenter(HomeContract.View view, CompositeSubscriptionManager subscriptionManager, RideManager rideManager,
      Preferences preferences, HomeNavigator homeNavigator) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.rideManager = rideManager;
    this.preferences = preferences;
    this.homeNavigator = homeNavigator;
    macValidator = Pattern.compile("(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}.){2}[0-9A-Fa-f]{4})");
  }

  @Override public void start() {
    handleStartClick();
    watchMiBandAddressChange();
    showMiBandAddress();
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  private void watchMiBandAddressChange() {
    Observable<Void> watchMiBandAddressChange = view.getMiBandAddressChanges().doOnNext(address -> {
      if (isValidBluetoothAddress(address)) {
        view.cleanMiBandAddressError();
        saveMiBandAddress(address);
      } else {
        view.showMiBandAddressError();
      }
    }).retry().map(__ -> null);

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> watchMiBandAddressChange)
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  private void handleStartClick() {
    Observable<Void> handleStartClick =
        view.handleStartClick().flatMap(__ -> view.getMiBandAddressChanges().first().toSingle().flatMap(address -> {
          // fixme un-comment bluetooth address validation before continuing
          //if (isValidBluetoothAddress(address)) {
          return rideManager.start(view.getRideName()).doOnSuccess(rideId -> {
            homeNavigator.forward(rideId);
          });
          //}
          //return Single.just(address);
        }).toObservable()).onErrorResumeNext(err -> handleStartRideError(err).map(__ -> null)).map(__ -> null);

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> handleStartClick)
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  private boolean isValidBluetoothAddress(String address) {
    return macValidator.matcher(address).matches();
  }

  private void saveMiBandAddress(String address) {
    preferences.setMiBandAddress(address);
  }

  private void showMiBandAddress() {
    Completable showMiBandAddress = Completable.fromAction(() -> view.showMiBandAddress(preferences.getMiBandAddressOrDefault()));

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> showMiBandAddress.toObservable())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
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
        .andThen(Completable.timer(2, TimeUnit.SECONDS).doOnCompleted(() -> showActivateGpsView()))
        .toObservable();
  }

  @Override public void showActivateGpsViewMessage() {
    view.showActivateGpsViewMessage();
  }

  @Override public void showActivateGpsView() {
    view.sendToGpsSettings();
  }
}
