package io.github.sithengineer.motoqueiro.cruising;

import io.github.sithengineer.motoqueiro.BasePresenter;
import io.github.sithengineer.motoqueiro.BaseView;
import io.github.sithengineer.motoqueiro.hardware.capture.LatLng;
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import io.github.sithengineer.motoqueiro.hardware.capture.RelativeCoordinates;
import rx.Completable;
import rx.Observable;

public class CruisingContract {
  interface View extends BaseView {
    Observable<Void> stopClick();

    void goToStatistics();
  }

  interface Presenter extends BasePresenter {

    Completable handleMiBandCapture(MiBandData bandData);

    Completable stopCruising();

    Completable handleGpsPositionCapture(LatLng capture);

    Completable handleAccelerometerCapture(RelativeCoordinates capture);
  }
}
