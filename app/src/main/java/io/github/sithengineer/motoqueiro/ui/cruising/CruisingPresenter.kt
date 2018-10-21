package io.github.sithengineer.motoqueiro.ui.cruising

import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

// TODO
// insert data capture in batches
// use an entity to handle data save / load logic, given a repository
class CruisingPresenter(private val view: CruisingContract.View,
    private val subscriptionManager: CompositeSubscriptionManager,
    private val navigator: CruisingNavigator) : CruisingContract.Presenter {

  override fun start() {
    handleStopClick()
  }

  override fun stop() {
    subscriptionManager.clearAll()
  }

  private fun handleStopClick() {
    subscriptionManager.add(
        view.stopClick()
            .doOnNext { view.setStopButtonDisabled() }
            .doOnNext { navigator.stopServiceToGatherData() }
            .doOnNext { navigator.goToStatistics() }
            .retry()
            .subscribeBy(
                onComplete = { },
                onError = { err -> Timber.e(err) }
            )
    )
  }
}
