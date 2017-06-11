package io.github.sithengineer.motoqueiro.cruising;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Button;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.statistics.StatisticsActivity;
import javax.inject.Inject;
import rx.Observable;

public class CruisingFragment extends BaseFragment<CruisingContract.Presenter>
    implements CruisingContract.View {

  @BindView(R.id.stop_button) Button stopButton;
  @Inject CruisingContract.Presenter presenter;

  public static CruisingFragment newInstance() {
    return new CruisingFragment();
  }

  @Override protected int getViewId() {
    return R.layout.fragment_capturing_data;
  }

  @Nullable @Override public CruisingContract.Presenter getPresenter() {
    return presenter;
  }

  @Override public Observable<Void> stopClick() {
    return RxView.clicks(stopButton);
  }

  @Override public void goToStatistics() {
    Intent i = new Intent(getActivity(), StatisticsActivity.class);
    startActivity(i);
  }
}
