package io.github.sithengineer.motoqueiro.cruising;

import android.os.Bundle;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.network.NetworkModule;

public class CruisingActivity extends BaseActivity {
  public static final String RIDE_ID = "ride_id";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_frame);

    CruisingFragment fragment =
        (CruisingFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

    if (fragment == null) {
      fragment = CruisingFragment.newInstance();
      getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();
    }

    String rideId = getIntent().getStringExtra(RIDE_ID);

    DaggerCruisingComponent.builder()
        .sensorModule(new SensorModule(this))
        .cruisingModule(new CruisingModule(fragment, rideId))
        .networkModule(new NetworkModule(this))
        .dataModule(new DataModule(this))
        .build()
        .inject(fragment);
  }
}
