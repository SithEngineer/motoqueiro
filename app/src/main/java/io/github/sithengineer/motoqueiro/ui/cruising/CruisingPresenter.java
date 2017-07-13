package io.github.sithengineer.motoqueiro.ui.cruising;

import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.data.RideManager;
import io.github.sithengineer.motoqueiro.data.DataManager;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import rx.Completable;
import timber.log.Timber;

// TODO
// insert data capture in batches
// use an entity to handle data save / load logic, given a repository
public class CruisingPresenter implements CruisingContract.Presenter {

  private final CruisingContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;
  private final CruisingNavigator navigator;
  private DataManager dataManager;
  private RideManager rideManager;
  private String rideId;

  public CruisingPresenter(CruisingContract.View view,
      CompositeSubscriptionManager subscriptionManager, DataManager dataManager,
      RideManager rideManager, String rideId, CruisingNavigator navigator) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.dataManager = dataManager;
    this.rideManager = rideManager;
    this.rideId = rideId;
    this.navigator = navigator;
  }

  @Override public void start() {
    handleStopClick();
    gatherDataWhileRiding();
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  private void gatherDataWhileRiding() {
    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> dataManager.gatherData())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  private void handleStopClick() {
    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> view.stopClick()
            .doOnNext(__2 -> view.setStopButtonDisabled())
            .doOnNext(__2 -> view.showUploadView())
            .flatMap(
                __2 -> stopCruising(rideId).doOnCompleted(() -> view.hideUploadView())
                    .doOnCompleted(() -> view.setStopButtonEnabled())
                    .toObservable())
            .retry())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public Completable stopCruising(String rideId) {
    return rideManager.stop(rideId)
        .doOnCompleted(() -> navigator.goToStatistics(true))
        .doOnError(err -> {
          Timber.e(err);
          navigator.goToStatistics(false);
        })
        .doOnTerminate(() -> {
          // the user pressed stop button in this chain of events and at this point we
          // already sync'ed the data with the server. we do not need to keep listening
          // to sensor updates so cleaning all subscriptions (to sensors) will stop them
          stop();
        });
  }
}
