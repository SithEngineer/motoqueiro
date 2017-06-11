package io.github.sithengineer.motoqueiro.home;

import io.github.sithengineer.motoqueiro.BasePresenter;
import io.github.sithengineer.motoqueiro.BaseView;
import rx.Completable;
import rx.Observable;

public interface HomeContract {

  interface View extends BaseView {
    /**
     * @return a {@link Completable} that should navigate after start data tracking is
     * pressed
     */
    Observable<Void> handleStartClick();

    void sendToGpsSettings();

    void showActivateGpsViewMessage();

    void goToCruisingActivity(String rideId);

    void showGenericError();

    String getRideName();

    String getMiBandAddress();
  }

  interface Presenter extends BasePresenter {
    void showActivateGpsViewMessage();

    void showActivateGpsView();
  }
}
