package io.github.sithengineer.motoqueiro.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.R;
import javax.inject.Inject;
import rx.Observable;
import rx.Single;

public class StatisticsFragment extends BaseFragment<StatisticsContract.Presenter>
    implements StatisticsContract.View {

  @BindView(R.id.go_home_button) Button goHomeButton;
  @BindView(R.id.upload_completed_message) TextView uploadCompletedMessage;
  @Inject StatisticsContract.Presenter presenter;
  private boolean uploadCompleted;

  public static StatisticsFragment newInstance(boolean dataUploadComplete) {
    Bundle args = new Bundle();
    args.putBoolean(StatisticsActivity.EXTRA_UPLOAD_COMPLETE, dataUploadComplete);
    StatisticsFragment fragment = new StatisticsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int getViewId() {
    return R.layout.fragment_statistics;
  }

  @Override public void inject() {
    MotoqueiroApp.get(getContext())
        .getRideComponent()
        .with(new StatisticsModule(this, new StatisticsNavigator(getActivity())))
        .inject(this);
  }

  @Nullable @Override public StatisticsContract.Presenter getPresenter() {
    return presenter;
  }

  @Override protected void loadArguments(@Nullable Bundle args) {
    uploadCompleted =
        args != null && args.getBoolean(StatisticsActivity.EXTRA_UPLOAD_COMPLETE, false);
  }

  @Override public Single<Boolean> isUploadCompleted() {
    return Single.just(uploadCompleted);
  }

  @Override public void setUploadCompletedMessageVisible() {
    uploadCompletedMessage.setVisibility(View.VISIBLE);
  }

  @Override public Observable<Void> goHomeClick() {
    return RxView.clicks(goHomeButton);
  }
}
