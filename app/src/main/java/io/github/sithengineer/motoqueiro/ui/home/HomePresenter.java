package io.github.sithengineer.motoqueiro.ui.home;

import android.Manifest;
import io.github.sithengineer.motoqueiro.PermissionAuthority;
import io.github.sithengineer.motoqueiro.PermissionResponse;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException;
import io.github.sithengineer.motoqueiro.hardware.DevicePosition;
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
  private final Preferences preferences;
  private final Pattern macValidator;
  private final HomeNavigator navigator;
  private final PermissionAuthority permissionAuthority;

  public HomePresenter(HomeContract.View view, CompositeSubscriptionManager subscriptionManager,
      Preferences preferences, HomeNavigator navigator, PermissionAuthority permissionAuthority) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.preferences = preferences;
    this.navigator = navigator;
    this.permissionAuthority = permissionAuthority;
    macValidator = Pattern.compile(
        "(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}.){2}[0-9A-Fa-f]{4})");
  }

  @Override public void start() {
    handleStartRideClick();
    handlePermissionsGiven();
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

    subscriptionManager.add(watchMiBandAddressChange.subscribe(__ -> {
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

  private void handleStartRideClick() {
    subscriptionManager.add(view.handleStartRideClick()
        .doOnNext(__ -> permissionAuthority.askForPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE))
        .doOnError(err -> Timber.e(err))
        .retry()
        .subscribe());
  }

  private String getRideName() {
    return view.getRideName();
  }

  private DevicePosition getDevicePosition() {
    return DevicePosition.fromValue(view.getSelectedDevicePosition());
  }

  private void handlePermissionsGiven() {
    subscriptionManager.add(allPermissionsAreAllowed().flatMapCompletable(allPermissionsGranted -> {
      if (allPermissionsGranted) {
        return Completable.fromAction(() -> {
          navigator.startServiceToGatherData(getRideName(), getDevicePosition());
          navigator.forward();
        });
      }
      return Completable.fromAction(() -> view.showGivePermissionsMessage());
    }).onErrorResumeNext(err -> handleStartRideError(err).toObservable()).subscribe(__ -> {
    }, err -> Timber.e(err)));
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

    subscriptionManager.add(showMiBandAddress.subscribe(() -> {
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
        .andThen(Completable.timer(2, TimeUnit.SECONDS).doOnCompleted(() -> showActivateGpsView()));
  }

  @Override public void showActivateGpsViewMessage() {
    view.showActivateGpsViewMessage();
  }

  @Override public void showActivateGpsView() {
    view.sendToGpsSettings();
  }
}
