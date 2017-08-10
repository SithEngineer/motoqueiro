package io.github.sithengineer.motoqueiro.ui.cruising;

import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import timber.log.Timber;

// TODO
// insert data capture in batches
// use an entity to handle data save / load logic, given a repository
public class CruisingPresenter implements CruisingContract.Presenter {

  private final CruisingContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;
  private final CruisingNavigator navigator;

  public CruisingPresenter(CruisingContract.View view,
      CompositeSubscriptionManager subscriptionManager, CruisingNavigator navigator) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.navigator = navigator;
  }

  @Override public void start() {
    handleStopClick();
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  private void handleStopClick() {
    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> view.stopClick()
            .doOnNext(__2 -> view.setStopButtonDisabled())
            .doOnNext(__2 -> navigator.stopServiceToGatherData())
            .doOnNext(__2 -> navigator.goToStatistics())
            .retry())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }
}
