package io.github.sithengineer.motoqueiro.cruising;

import com.trello.rxlifecycle.android.FragmentEvent;
import io.github.sithengineer.motoqueiro.home.RideManager;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Inject;
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

  @Inject public CruisingPresenter(CruisingContract.View view,
      CompositeSubscriptionManager subscriptionManager, DataManager dataManager,
      RideManager rideManager, String rideId) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.dataManager = dataManager;
    this.rideManager = rideManager;
    this.rideId = rideId;
  }

  @Override public void start() {

    Observable<Void> completeRide =
        view.stopClick().flatMap(__ -> stopCruising(rideId).toObservable());

    subscriptionManager.add(
        view.lifecycle()
            .filter(event -> event == FragmentEvent.CREATE_VIEW)

            .flatMap(event -> Observable.merge(completeRide, dataManager.gatherData()))
            .doOnUnsubscribe(() -> Timber.i("Unsubscribed from Cruising Presenter"))
            .compose(view.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .subscribe(__ -> {
            }, err -> Timber.e(err)));
  }

  @Override public void stop() {
    subscriptionManager.clearAll();
  }

  @Override public Completable stopCruising(String rideId) {
    return rideManager.stop(rideId).doOnCompleted(() -> {
      // the user pressed stop button in this chain of events and at this point we
      // already sync'ed the data with the server. we do not need to keep listening
      // to sensor updates so cleaning all subscriptions (to sensors) will stop them
      stop();

      view.goToStatistics();
    });
  }
}
