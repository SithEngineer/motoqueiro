package io.github.sithengineer.motoqueiro.ui.statistics;

import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.R;
import javax.inject.Inject;
import rx.Observable;

public class StatisticsFragment extends BaseFragment<StatisticsContract.Presenter>
    implements StatisticsContract.View {

  @BindView(R.id.go_home_button) Button goHomeButton;
  @BindView(R.id.upload_completed_message) TextView uploadCompletedMessage;
  @Inject StatisticsContract.Presenter presenter;

  public static StatisticsFragment newInstance() {
    return new StatisticsFragment();
  }

  @Override protected int getViewId() {
    return R.layout.fragment_statistics;
  }

  @Override public void inject() {
    MotoqueiroApp.get(getContext()).createUiComponent()
        .with(new StatisticsModule(this, new StatisticsNavigator(getActivity())))
        .inject(this);
  }

  @Nullable @Override public StatisticsContract.Presenter getPresenter() {
    return presenter;
  }

  @Override public Observable<Void> goHomeClick() {
    return RxView.clicks(goHomeButton);
  }
}
