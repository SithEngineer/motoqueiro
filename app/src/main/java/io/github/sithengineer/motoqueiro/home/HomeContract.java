package io.github.sithengineer.motoqueiro.home;

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

    void showGenericError();

    String getRideName();

    void showMiBandAddress(String miBandAddress);

    Observable<String> getMiBandAddressChanges();

    void showMiBandAddressError();
    void cleanMiBandAddressError();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    void showActivateGpsViewMessage();

    void showActivateGpsView();
  }
}
