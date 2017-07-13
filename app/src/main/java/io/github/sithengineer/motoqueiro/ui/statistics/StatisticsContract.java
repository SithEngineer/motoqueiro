package io.github.sithengineer.motoqueiro.ui.statistics;

import rx.Observable;
import rx.Single;

public interface StatisticsContract {
  interface View extends io.github.sithengineer.motoqueiro.View {
    Single<Boolean> isUploadCompleted();

    void setUploadCompletedMessageVisible();

    Observable<Void> goHomeClick();
  }

  interface Presenter extends io.github.sithengineer.motoqueiro.Presenter {
    void goHome();
  }
}
