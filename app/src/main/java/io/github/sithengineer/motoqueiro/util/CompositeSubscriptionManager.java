package io.github.sithengineer.motoqueiro.util;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CompositeSubscriptionManager {

  private final CompositeSubscription subscriptions = new CompositeSubscription();

  public void add(Subscription subscription) {
    subscriptions.add(subscription);
  }

  public void clearAll() {
    if (subscriptions.hasSubscriptions() && !subscriptions.isUnsubscribed()) {
      subscriptions.unsubscribe();
    }
  }
}
