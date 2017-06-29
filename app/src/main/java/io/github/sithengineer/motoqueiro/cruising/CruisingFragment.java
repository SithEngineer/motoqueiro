package io.github.sithengineer.motoqueiro.cruising;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.BluetoothModule;
import javax.inject.Inject;
import rx.Observable;

public class CruisingFragment extends BaseFragment<CruisingContract.Presenter> implements CruisingContract.View {

  @BindView(R.id.stop_button) Button stopButton;
  @Inject CruisingContract.Presenter presenter;

  public static CruisingFragment newInstance(String rideId) {
    Bundle args = new Bundle();
    args.putString(CruisingActivity.EXTRA_RIDE_ID, rideId);

    CruisingFragment fragment = new CruisingFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int getViewId() {
    return R.layout.fragment_capturing_data;
  }

  @Override public void inject() {
    Bundle args = getArguments();
    if (args != null) {
      String rideId = args.getString(CruisingActivity.EXTRA_RIDE_ID);

      final Context context = getContext();
      MotoqueiroApp.get(context)
          .getRideComponent()
          .with(new CruisingModule(this, rideId, new CruisingNavigator(getActivity())), new BluetoothModule(context))
          .inject(this);
    } else {
      throw new IllegalStateException("Unable to start cruising without ride id");
    }
  }

  @Nullable @Override public CruisingContract.Presenter getPresenter() {
    return presenter;
  }

  @Override public Observable<Void> stopClick() {
    return RxView.clicks(stopButton);
  }
}
