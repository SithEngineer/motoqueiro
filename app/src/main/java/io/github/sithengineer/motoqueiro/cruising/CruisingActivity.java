package io.github.sithengineer.motoqueiro.cruising;

import android.os.Bundle;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;

public class CruisingActivity extends BaseActivity {
  public static final String EXTRA_RIDE_ID = "ride_id";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_frame);

    CruisingFragment fragment =
        (CruisingFragment) getSupportFragmentManager().findFragmentById(
            R.id.content_frame);

    String rideId = getIntent().getStringExtra(EXTRA_RIDE_ID);

    if (fragment == null) {
      fragment = CruisingFragment.newInstance(rideId);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.content_frame, fragment)
          .commit();
    }
  }
}
