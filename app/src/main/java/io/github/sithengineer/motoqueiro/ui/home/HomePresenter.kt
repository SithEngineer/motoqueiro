package io.github.sithengineer.motoqueiro.ui.home

import android.Manifest
import io.github.sithengineer.motoqueiro.PermissionAuthority
import io.github.sithengineer.motoqueiro.PermissionResponse
import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.exception.GpsNotActiveException
import io.github.sithengineer.motoqueiro.hardware.DevicePosition
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class HomePresenter(private val view: HomeContract.View,
    private val subscriptionManager: CompositeSubscriptionManager,
    private val preferences: Preferences, private val navigator: HomeNavigator,
    private val permissionAuthority: PermissionAuthority) : HomeContract.Presenter {

  private val macValidator: Pattern = Pattern.compile(
      "(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}.){2}[0-9A-Fa-f]{4})")

  private val rideName: String
    get() = view.getRideName()

  private val devicePosition: DevicePosition
    get() = DevicePosition.fromValue(view.selectedDevicePosition())

  override fun start() {
    handleStartRideClick()
    handlePermissionsGiven()
    watchMiBandAddressChange()
    showMiBandAddress()
  }

  override fun stop() {
    subscriptionManager.clearAll()
  }

  private fun watchMiBandAddressChange() {
    val watchMiBandAddressChange = view.miBandAddressChanges().doOnNext { address ->
      if (isValidBluetoothAddress(address)) {
        view.cleanMiBandAddressError()
        saveMiBandAddress(address)
      } else {
        view.showMiBandAddressError()
      }
    }.retry().map<Void> { _ -> null }

    subscriptionManager.add(
        watchMiBandAddressChange
            .subscribeBy(
                onComplete = { },
                onError = { err -> Timber.e(err) }
            )
    )
  }

  private fun areAllGranted(permissionResponses: List<PermissionResponse>): Boolean {
    for ((_, _, isGranted) in permissionResponses) {
      if (!isGranted) {
        return false
      }
    }

    return true
  }

  private fun allPermissionsAreAllowed(): Observable<Boolean> {
    return permissionAuthority.getPermissionResult(PERMISSION_REQUEST_CODE)
        .buffer(PERMISSIONS.size)
        .flatMapSingle { permissionResponses ->
          return@flatMapSingle if (areAllGranted(permissionResponses)) {
            Single.just<Boolean>(java.lang.Boolean.TRUE)
          } else {
            Single.just(java.lang.Boolean.FALSE)
          }
        }
  }

  private fun handleStartRideClick() {
    subscriptionManager.add(view.handleStartRideClick()
        .doOnNext { _ ->
          permissionAuthority.askForPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE)
        }
        .doOnError { err -> Timber.e(err) }
        .retry()
        .subscribe())
  }

  private fun handlePermissionsGiven() {
    subscriptionManager.add(
        allPermissionsAreAllowed()
            .flatMapCompletable { allPermissionsGranted ->
              return@flatMapCompletable if (allPermissionsGranted) {
                Completable.fromAction {
                  navigator.startServiceToGatherData(rideName, devicePosition)
                  navigator.forward()
                }
              } else {
                Completable.fromAction { view.showGivePermissionsMessage() }
              }
            }
            .onErrorResumeNext { err -> handleStartRideError(err) }
            .subscribeBy(
                onComplete = { },
                onError = { err -> Timber.e(err) }
            )
    )
  }

  private fun isValidBluetoothAddress(address: String): Boolean {
    return macValidator.matcher(address).matches()
  }

  private fun saveMiBandAddress(address: String) {
    preferences.miBandAddress = address
  }

  private fun showMiBandAddress() {
    val showMiBandAddress = Completable.fromAction {
      view.showMiBandAddress(preferences.miBandAddress)
    }

    subscriptionManager.add(
        showMiBandAddress
            .subscribeBy(
                onComplete = { },
                onError = { err -> Timber.e(err) }
            )
    )
  }

  private fun handleStartRideError(err: Throwable): Completable {
    return Completable.defer {
      return@defer if (err is GpsNotActiveException) {
        sendToActivateGpsSettings()
      } else {
        Completable.fromAction {
          Timber.e(err)
          view.showGenericError()
        }
      }
    }
  }

  private fun sendToActivateGpsSettings(): Completable {
    return Completable.fromAction { showActivateGpsViewMessage() }
        .andThen(Completable.timer(2, TimeUnit.SECONDS).doOnComplete { showActivateGpsView() })
  }

  override fun showActivateGpsViewMessage() {
    view.showActivateGpsViewMessage()
  }

  override fun showActivateGpsView() {
    view.sendToGpsSettings()
  }

  companion object {

    private const val PERMISSION_REQUEST_CODE = 101

    private val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.WRITE_SYNC_SETTINGS,
        Manifest.permission.INTERNET, Manifest.permission.GET_ACCOUNTS)
  }
}
