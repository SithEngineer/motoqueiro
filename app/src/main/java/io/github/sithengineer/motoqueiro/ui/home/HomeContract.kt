package io.github.sithengineer.motoqueiro.ui.home

import io.reactivex.Observable

interface HomeContract {

  interface View : io.github.sithengineer.motoqueiro.ui.View {

    fun miBandAddressChanges(): Observable<String>

    fun selectedDevicePosition(): Int

    fun getRideName(): String

    fun handleStartRideClick(): Observable<Any>

    fun sendToGpsSettings()

    fun showActivateGpsViewMessage()

    fun showGenericError()

    fun showMiBandAddress(miBandAddress: String)

    fun showMiBandAddressError()

    fun cleanMiBandAddressError()

    fun showGivePermissionsMessage()
  }

  interface Presenter : io.github.sithengineer.motoqueiro.ui.Presenter {
    fun showActivateGpsViewMessage()

    fun showActivateGpsView()
  }
}
