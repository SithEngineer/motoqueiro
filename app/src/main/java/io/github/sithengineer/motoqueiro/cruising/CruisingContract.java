package io.github.sithengineer.motoqueiro.cruising;

import io.github.sithengineer.motoqueiro.BasePresenter;
import io.github.sithengineer.motoqueiro.BaseView;
import rx.Completable;
import rx.Observable;

public class CruisingContract {
  interface View extends BaseView {
    Observable<Void> stopClick();

    void goToStatistics();
  }

  interface Presenter extends BasePresenter {
    Completable stopCruising(String rideId);
  }
}
