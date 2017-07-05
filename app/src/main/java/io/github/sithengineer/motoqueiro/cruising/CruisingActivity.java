package io.github.sithengineer.motoqueiro.cruising;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;

public class CruisingActivity extends BaseActivity {
  static final String EXTRA_RIDE_ID = "ride_id";

  public static Intent getNavigationIntent(Context context, String rideId) {
    Intent intent = new Intent(context, CruisingActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.putExtra(EXTRA_RIDE_ID, rideId);
    return intent;
  }

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

  @Override public void onBackPressed() {
    // does nothing
    // super.onBackPressed();
    // new CruisingNavigator(this).goToStatistics(false);
  }
}
