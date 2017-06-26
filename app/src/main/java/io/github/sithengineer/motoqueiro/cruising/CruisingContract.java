package io.github.sithengineer.motoqueiro.cruising;

import io.github.sithengineer.motoqueiro.BaseView;
import rx.Completable;
import rx.Observable;

public class CruisingContract {
  interface View extends BaseView {
    Observable<Void> stopClick();

    void goToStatistics(boolean uploadCompleted);
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    Completable stopCruising(String rideId);
  }
}
