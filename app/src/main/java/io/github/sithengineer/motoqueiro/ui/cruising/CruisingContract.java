package io.github.sithengineer.motoqueiro.ui.cruising;

import rx.Observable;

public class CruisingContract {
  interface View extends io.github.sithengineer.motoqueiro.View {

    void setStopButtonEnabled();

    void setStopButtonDisabled();

    Observable<Void> stopClick();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
  }
}
