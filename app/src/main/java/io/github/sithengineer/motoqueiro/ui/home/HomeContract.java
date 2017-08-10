package io.github.sithengineer.motoqueiro.ui.home;

import rx.Observable;

public interface HomeContract {

  interface View extends io.github.sithengineer.motoqueiro.View {

    Observable<Void> handleStartRideClick();

    void sendToGpsSettings();

    void showActivateGpsViewMessage();

    void showGenericError();

    void showMiBandAddress(String miBandAddress);

    Observable<String> getMiBandAddressChanges();

    void showMiBandAddressError();

    void cleanMiBandAddressError();

    void showGivePermissionsMessage();

    String getRideName();

    int getSelectedDevicePosition();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    void showActivateGpsViewMessage();

    void showActivateGpsView();
  }
}
