package io.github.sithengineer.motoqueiro.ui.cruising

import io.reactivex.Observable

interface CruisingContract {
  interface View : io.github.sithengineer.motoqueiro.ui.View {

    fun setStopButtonEnabled()

    fun setStopButtonDisabled()

    fun stopClick(): Observable<Any>
  }

  interface Presenter : io.github.sithengineer.motoqueiro.ui.Presenter
}
