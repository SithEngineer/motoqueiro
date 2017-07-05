package io.github.sithengineer.motoqueiro.home;

import android.Manifest;
import android.support.annotation.NonNull;
import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.PermissionAuthority;
import io.github.sithengineer.motoqueiro.PermissionResponse;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import rx.Completable;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

public class HomePresenter implements HomeContract.Presenter {

  private static final int PERMISSION_REQUEST_CODE = 101;

  private static final String[] PERMISSIONS = new String[] {
      Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN,
      Manifest.permission.WRITE_SYNC_SETTINGS, Manifest.permission.INTERNET,
      Manifest.permission.GET_ACCOUNTS
  };

  private final CompositeSubscriptionManager subscriptionManager;
  private final HomeContract.View view;
  private final RideManager rideManager;
  private final Preferences preferences;
  private final Pattern macValidator;
  private final HomeNavigator homeNavigator;
  private final PermissionAuthority permissionAuthority;

  public HomePresenter(HomeContract.View view,
      CompositeSubscriptionManager subscriptionManager, RideManager rideManager,
      Preferences preferences, HomeNavigator homeNavigator,
      PermissionAuthority permissionAuthority) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.rideManager = rideManager;
    this.preferences = preferences;
    this.homeNavigator = homeNavigator;
    this.permissionAuthority = permissionAuthority;
    macValidator = Pattern.compile(
        "(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}.){2}[0-9A-Fa-f]{4})");
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
        .flatMap(__ -> watchMiBandAddressChange)
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  private boolean areAllGranted(List<PermissionResponse> permissionResponses) {
    for (final PermissionResponse permissionResponse : permissionResponses) {
      if (!permissionResponse.isGranted()) {
        return false;
      }
    }

    return true;
  }

  private Observable<Boolean> allPermissionsAreAllowed() {
    return permissionAuthority.getPermissionResult(PERMISSION_REQUEST_CODE)
        .buffer(PERMISSIONS.length)
        .flatMapSingle(permissionResponses -> {
          if (areAllGranted(permissionResponses)) {
            return Single.just(Boolean.TRUE);
          }
          return Single.just(Boolean.FALSE);
        });
  }

  private void handleStartClick() {
    Observable<Void> handlePermissionsResult =
        allPermissionsAreAllowed().flatMapCompletable(allPermissionsGranted -> {
          if (allPermissionsGranted) {
            return startRide();
          }
          return Completable.fromAction(() -> view.showGivePermissionsMessage());
        })
            .onErrorResumeNext(err -> handleStartRideError(err).toObservable())
            .map(__ -> null);

    Observable<Void> handleStartClickToAskPermissions = view.handleStartClick()
        .doOnNext(__ -> permissionAuthority.askForPermissions(PERMISSIONS,
            PERMISSION_REQUEST_CODE))
        .map(__ -> null);

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> Observable.merge(handleStartClickToAskPermissions,
            handlePermissionsResult))
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @NonNull private Completable startRide() {
    return view.getMiBandAddressChanges().first().toSingle().flatMap(address -> {
      // fixme un-comment bluetooth address validation before continuing
      //if (isValidBluetoothAddress(address)) {
      return rideManager.start(view.getRideName()).doOnSuccess(rideId -> {
        homeNavigator.forward(rideId);
      });
      //}
      //return Single.just(address);
    }).toCompletable();
  }

  private boolean isValidBluetoothAddress(String address) {
    return macValidator.matcher(address).matches();
  }

  private void saveMiBandAddress(String address) {
    preferences.setMiBandAddress(address);
  }

  private void showMiBandAddress() {
    Completable showMiBandAddress = Completable.fromAction(
        () -> view.showMiBandAddress(preferences.getMiBandAddressOrDefault()));

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> showMiBandAddress.toObservable())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  private Completable handleStartRideError(Throwable err) {
    return Completable.defer(() -> {
      if (err instanceof GpsNotActiveException) {
        return sendToActivateGpsSettings();
      }

      return Completable.fromAction(() -> {
        Timber.e(err);
        view.showGenericError();
      });
    });
  }

  private Completable sendToActivateGpsSettings() {
    return Completable.fromAction(() -> showActivateGpsViewMessage())
        .andThen(Completable.timer(2, TimeUnit.SECONDS)
            .doOnCompleted(() -> showActivateGpsView()));
  }

  @Override public void showActivateGpsViewMessage() {
    view.showActivateGpsViewMessage();
  }

  @Override public void showActivateGpsView() {
    view.sendToGpsSettings();
  }
}
