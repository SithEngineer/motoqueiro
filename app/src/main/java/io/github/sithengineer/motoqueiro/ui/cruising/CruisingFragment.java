package io.github.sithengineer.motoqueiro.ui.cruising;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import io.github.sithengineer.motoqueiro.BaseFragment;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule;
import javax.inject.Inject;
import rx.Observable;

public class CruisingFragment extends BaseFragment<CruisingContract.Presenter>
    implements CruisingContract.View {

  @BindView(R.id.stop_button) Button stopButton;
  @BindView(R.id.upload_view_group) View uploadView;
  @Inject CruisingContract.Presenter presenter;

  public static CruisingFragment newInstance() {
    return new CruisingFragment();
  }

  @Override protected int getViewId() {
    return R.layout.fragment_capturing_data;
  }

  @Override public void inject() {
    final Context context = getContext();
    MotoqueiroApp.get(context)
        .createUiComponent()
        .with(new CruisingModule(this, new CruisingNavigator(getActivity())),
            new MiBandModule(context))
        .inject(this);
  }

  @Nullable @Override public CruisingContract.Presenter getPresenter() {
    return presenter;
  }

  @Override public void setStopButtonEnabled() {
    stopButton.setEnabled(true);
  }

  @Override public void setStopButtonDisabled() {
    stopButton.setEnabled(false);
  }

  @Override public Observable<Void> stopClick() {
    return RxView.clicks(stopButton);
  }
}
