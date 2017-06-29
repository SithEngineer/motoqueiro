package io.github.sithengineer.motoqueiro.cruising;

import android.support.annotation.NonNull;
import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.home.RideManager;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

// TODO
// insert data capture in batches
// use an entity to handle data save / load logic, given a repository
public class CruisingPresenter implements CruisingContract.Presenter {

  private final CruisingContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;
  private DataManager dataManager;
  private RideManager rideManager;
  private String rideId;

  public CruisingPresenter(CruisingContract.View view, CompositeSubscriptionManager subscriptionManager, DataManager dataManager,
      RideManager rideManager, String rideId, CruisingNavigator navigator) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.dataManager = dataManager;
    this.rideManager = rideManager;
    this.rideId = rideId;
  }

  @Override public void start() {
    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> handleStopClick())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));

    subscriptionManager.add(view.lifecycle()
        .filter(event -> event == FragmentEvent.CREATE_VIEW)
        .flatMap(__ -> dataManager.gatherData())
        .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(__ -> {
        }, err -> Timber.e(err)));
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  @NonNull private Observable<Void> handleStopClick() {
    return view.stopClick()
        .flatMap(__ -> stopCruising(rideId).toObservable())
        .retry()
        .map(__ -> null);
  }

  @Override public Completable stopCruising(String rideId) {
    return rideManager.stop(rideId).doOnCompleted(() -> {
      view.goToStatistics(true);
    }).doOnError(err -> {
      Timber.e(err);
      view.goToStatistics(false);
    }).doOnTerminate(() -> {
      // the user pressed stop button in this chain of events and at this point we
      // already sync'ed the data with the server. we do not need to keep listening
      // to sensor updates so cleaning all subscriptions (to sensors) will stop them
      stop();
    });
  }
}
