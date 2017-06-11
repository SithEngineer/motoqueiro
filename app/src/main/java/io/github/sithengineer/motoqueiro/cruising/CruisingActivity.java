package io.github.sithengineer.motoqueiro.cruising;

import android.os.Bundle;
import android.text.TextUtils;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.MotoqueiroApp;
import io.github.sithengineer.motoqueiro.R;

public class CruisingActivity extends BaseActivity {
  public static final String EXTRA_RIDE_ID = "ride_id";
  public static final String EXTRA_MI_BAND_ADDRESS = "mi_band_address";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_frame);

    CruisingFragment fragment =
        (CruisingFragment) getSupportFragmentManager().findFragmentById(
            R.id.content_frame);

    if (fragment == null) {
      fragment = CruisingFragment.newInstance();
      getSupportFragmentManager().beginTransaction()
          .add(R.id.content_frame, fragment)
          .commit();
    }

    String rideId = getIntent().getStringExtra(EXTRA_RIDE_ID);
    String miBandAddress = getIntent().getStringExtra(EXTRA_MI_BAND_ADDRESS);

    if (TextUtils.isEmpty(miBandAddress)) {
      miBandAddress = "F0:E3:C8:01:11:0D";
    }

    MotoqueiroApp.get(this)
        .createDataComponent(miBandAddress)
        .with(new CruisingModule(fragment, rideId))
        .inject(fragment);
  }
}
