package io.github.sithengineer.motoqueiro.statistics;

import io.github.sithengineer.motoqueiro.BaseView;
import rx.Observable;
import rx.Single;

public interface StatisticsContract {
  interface View extends BaseView {
    Single<Boolean> isUploadCompleted();

    void setUploadCompletedMessageVisible();

    Observable<Void> goHomeClick();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    void goHome();
  }
}
