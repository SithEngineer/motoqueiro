package io.github.sithengineer.motoqueiro.statistics;

import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import timber.log.Timber;

public class StatisticsPresenter implements StatisticsContract.Presenter {

  private final StatisticsContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;

  public StatisticsPresenter(StatisticsContract.View view, CompositeSubscriptionManager subscriptionManager) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
  }

  @Override public void start() {
    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.RESUME)
        .flatMap(event -> view.goHomeClick().doOnNext(__ -> goHome()))
        .compose(view.bindUntilEvent(FragmentEvent.PAUSE))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  @Override public void goHome() {
    view.navigateToHome();
  }
}
