package io.github.sithengineer.motoqueiro.cruising;

import rx.Completable;
import rx.Observable;

public class CruisingContract {
  interface View extends io.github.sithengineer.motoqueiro.View {
    void showUploadView();

    void hideUploadView();

    void setStopButtonEnabled();

    void setStopButtonDisabled();

    Observable<Void> stopClick();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    Completable stopCruising(String rideId);
  }
}
