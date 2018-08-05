package io.github.sithengineer.motoqueiro.ui.statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;

public class StatisticsActivity extends BaseActivity {

  public static Intent getNavigationIntent(Context context) {
    Intent intent = new Intent(context, StatisticsActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_frame);

    StatisticsFragment fragment =
        (StatisticsFragment) getSupportFragmentManager().findFragmentById(
            R.id.content_frame);

    Intent intent = getIntent();
    if (fragment == null) {
      fragment = StatisticsFragment.newInstance();

      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.addToBackStack("statistics_activity_fragment")
          .replace(R.id.content_frame, fragment)
          .commit();
    }
  }
}
