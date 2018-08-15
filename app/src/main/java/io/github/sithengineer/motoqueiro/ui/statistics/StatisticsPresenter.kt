package io.github.sithengineer.motoqueiro.ui.statistics

import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class StatisticsPresenter(private val view: StatisticsContract.View,
    private val subscriptionManager: CompositeSubscriptionManager,
    private val navigator: StatisticsNavigator) : StatisticsContract.Presenter {

  override fun start() {
    handleGoHomeClicks()
  }

  override fun stop() {
    subscriptionManager.clearAll()
  }

  private fun handleGoHomeClicks() {
    subscriptionManager.add(
        view.goHomeClick().subscribeBy(
            onNext = { goHome() },
            onError = { error -> Timber.e(error) }
        )
    )
  }

  override fun goHome() {
    navigator.navigateToHome()
  }
}
