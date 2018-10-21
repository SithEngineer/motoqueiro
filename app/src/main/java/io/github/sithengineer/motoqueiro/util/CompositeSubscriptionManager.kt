package io.github.sithengineer.motoqueiro.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CompositeSubscriptionManager {

  private val subscriptions = CompositeDisposable()

  fun add(subscription: Disposable) {
    subscriptions.add(subscription)
  }

  fun clearAll() {
    if (subscriptions.size() > 0 && !subscriptions.isDisposed) {
      subscriptions.dispose()
    }
  }
}
