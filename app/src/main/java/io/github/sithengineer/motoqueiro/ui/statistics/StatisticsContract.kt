package io.github.sithengineer.motoqueiro.ui.statistics

import io.reactivex.Observable

interface StatisticsContract {
  interface View : io.github.sithengineer.motoqueiro.ui.View {
    fun goHomeClick(): Observable<Any>
  }

  interface Presenter : io.github.sithengineer.motoqueiro.ui.Presenter {
    fun goHome()
  }
}
