package io.github.sithengineer.motoqueiro.ui.statistics;

import com.trello.rxlifecycle.android.FragmentEvent;
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
    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(event -> view.goHomeClick().doOnNext(__ -> goHome()))
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public void goHome() {
    navigator.navigateToHome();
  }
}
