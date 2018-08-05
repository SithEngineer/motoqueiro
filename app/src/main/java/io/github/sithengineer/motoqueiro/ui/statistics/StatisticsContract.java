package io.github.sithengineer.motoqueiro.ui.statistics;

import rx.Observable;

public interface StatisticsContract {
  interface View extends io.github.sithengineer.motoqueiro.View {
    Observable<Void> goHomeClick();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    void goHome();
  }
}
