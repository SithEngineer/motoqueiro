package io.github.sithengineer.motoqueiro.home;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.cruising.CruisingActivity;
import javax.inject.Inject;
import rx.Observable;

public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

  @BindView(R.id.start_button) Button startButton;
  @Inject HomeContract.Presenter presenter;

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Override public int getViewId() {
    return R.layout.fragment_home;
  }

  @Nullable @Override public HomeContract.Presenter getPresenter() {
    return presenter;
  }

  @Override public Observable<Void> handleStartClick() {
    return RxView.clicks(startButton);
  }

  @Override public void sendToGpsSettings() {
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivity(intent);
  }

  @Override public void showActivateGpsViewMessage() {
    Snackbar.make(startButton, "Activate GPS", Snackbar.LENGTH_LONG).show();
  }

  @Override public void goToCruisingActivity(String rideId) {
    Intent i = new Intent(getActivity(), CruisingActivity.class);
    i.putExtra(CruisingActivity.RIDE_ID, rideId);
    startActivity(i);
  }
}
