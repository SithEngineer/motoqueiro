package io.github.sithengineer.motoqueiro.statistics;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Button;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.home.HomeActivity;
import javax.inject.Inject;
import rx.Observable;

public class StatisticsFragment extends BaseFragment<StatisticsContract.Presenter> implements StatisticsContract.View {

  @BindView(R.id.go_home_button) Button goHomeButton;
  @Inject StatisticsContract.Presenter presenter;

  public static StatisticsFragment newInstance() {
    return new StatisticsFragment();
  }

  @Nullable @Override public StatisticsContract.Presenter getPresenter() {
    return presenter;
  }

  @Override protected int getViewId() {
    return R.layout.fragment_statistics;
  }

  @Override public Observable<Void> goHomeClick() {
    return RxView.clicks(goHomeButton);
  }

  @Override public void navigateToHome() {
    Intent i = new Intent(getActivity(), HomeActivity.class);
    startActivity(i);
  }
}
