package io.github.sithengineer.motoqueiro.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.PermissionAuthority;
import io.github.sithengineer.motoqueiro.R;
import javax.inject.Inject;
import rx.Observable;

public class HomeFragment extends BaseFragment<HomeContract.Presenter>
    implements HomeContract.View {

  @BindView(R.id.miband_address_input_wrapper) TextInputLayout miBandAddressLayout;
  @BindView(R.id.miband_address_input) EditText miBandAddress;
  @BindView(R.id.ride_name_input_wrapper) TextInputLayout rideNameLayout;
  @BindView(R.id.ride_name_input) EditText rideName;
  @BindView(R.id.start_ride) Button startRideButton;
  @BindView(R.id.phone_position_spinner) Spinner phonePosition;
  @Inject HomeContract.Presenter presenter;

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Override public int getViewId() {
    return R.layout.fragment_home;
  }

  @Override public void inject() {
    MotoqueiroApp.get(getContext())
        .createUiComponent()
        .with(new HomeModule(this, new HomeNavigator(getActivity()),
            (PermissionAuthority) getActivity()))
        .inject(this);
  }

  @Nullable @Override public HomeContract.Presenter getPresenter() {
    return presenter;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = super.onCreateView(inflater, container, savedInstanceState);
    ArrayAdapter<CharSequence> adapter =
        ArrayAdapter.createFromResource(getActivity(), R.array.home_wheres_phone_array,
            android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    phonePosition.setAdapter(adapter);
    return view;
  }

  @Override public Observable<Void> handleStartRideClick() {
    return RxView.clicks(startRideButton);
  }

  @Override public void sendToGpsSettings() {
    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
  }

  @Override public void showActivateGpsViewMessage() {
    Snackbar.make(startRideButton, R.string.home_activate_gps, Snackbar.LENGTH_LONG).show();
  }

  @Override public void showGenericError() {
    Snackbar.make(startRideButton, R.string.home_generic_error, Snackbar.LENGTH_LONG).show();
  }

  @Override public void showMiBandAddress(String miBandAddress) {
    this.miBandAddress.setText(miBandAddress);
  }

  @Override public Observable<String> getMiBandAddressChanges() {
    return RxTextView.textChanges(miBandAddress).map(text -> text.toString());
  }

  @Override public void showMiBandAddressError() {
    miBandAddressLayout.setError(getString(R.string.home_view_heart_rate_band_address_error));
  }

  @Override public void cleanMiBandAddressError() {
    miBandAddressLayout.setError(null);
  }

  @Override public void showGivePermissionsMessage() {
    Snackbar.make(startRideButton, R.string.home_need_all_permissions, Snackbar.LENGTH_LONG).show();
  }

  @Override public String getRideName() {
    return rideName.getText().toString();
  }

  @Override public int getSelectedDevicePosition() {
    return phonePosition.getSelectedItemPosition();
  }
}
