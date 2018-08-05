package io.github.sithengineer.motoqueiro.ui.statistics;

import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import timber.log.Timber;

public class StatisticsPresenter implements StatisticsContract.Presenter {

  private final StatisticsContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;
  private final StatisticsNavigator navigator;

  public StatisticsPresenter(StatisticsContract.View view,
      CompositeSubscriptionManager subscriptionManager, StatisticsNavigator navigator) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.navigator = navigator;
  }

  @Override public void start() {
    handleGoHomeClicks();
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  private void handleGoHomeClicks() {
    subscriptionManager.add(
        view.goHomeClick().doOnNext(__ -> goHome()).doOnError(err -> Timber.e(err)).subscribe());
  }

  @Override public void goHome() {
    navigator.navigateToHome();
  }
}
