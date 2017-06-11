package io.github.sithengineer.motoqueiro.home;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.cruising.CruisingActivity;
import javax.inject.Inject;
import rx.Observable;

public class HomeFragment extends BaseFragment<HomeContract.Presenter>
    implements HomeContract.View {

  @BindView(R.id.miband_address_input_wrapper) TextInputLayout miBandAddressLayout;
  @BindView(R.id.miband_address_input) EditText miBandAddress;
  @BindView(R.id.ride_name_input_wrapper) TextInputLayout rideNameLayout;
  @BindView(R.id.ride_name_input) EditText rideName;
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
    Snackbar.make(startButton, R.string.home_activateGps_snackbar, Snackbar.LENGTH_LONG)
        .show();
  }

  @Override public void goToCruisingActivity(String rideId, String miBandAddress) {
    Intent i = new Intent(getActivity(), CruisingActivity.class);
    i.putExtra(CruisingActivity.EXTRA_RIDE_ID, rideId);
    i.putExtra(CruisingActivity.EXTRA_MI_BAND_ADDRESS, miBandAddress);
    startActivity(i);
  }

  @Override public void showGenericError() {
    Snackbar.make(startButton, R.string.home_genericError_snackBar, Snackbar.LENGTH_LONG)
        .show();
  }

  @Override public String getRideName() {
    return rideName.getText().toString();
  }

  @Override public String getMiBandAddress() {
    return miBandAddress.getText().toString();
  }
}
