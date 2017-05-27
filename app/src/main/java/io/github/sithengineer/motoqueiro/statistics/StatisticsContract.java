package io.github.sithengineer.motoqueiro.statistics;

import io.github.sithengineer.motoqueiro.BasePresenter;
import io.github.sithengineer.motoqueiro.BaseView;
import rx.Observable;

public interface StatisticsContract {
  interface View extends BaseView {
    Observable<Void> goHomeClick();

    void navigateToHome();
  }

  interface Presenter extends BasePresenter {
    void goHome();
  }
}
