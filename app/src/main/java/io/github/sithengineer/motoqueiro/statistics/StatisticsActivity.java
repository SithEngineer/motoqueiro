package io.github.sithengineer.motoqueiro.statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;

public class StatisticsActivity extends BaseActivity {
  static final String EXTRA_UPLOAD_COMPLETE = "upload_complete";

  public static Intent getNavigationIntent(Context context, boolean uploadComplete) {
    Intent intent = new Intent(context, StatisticsActivity.class);
    intent.putExtra(StatisticsActivity.EXTRA_UPLOAD_COMPLETE, uploadComplete);
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
      boolean dataUploadComplete =
          intent != null && intent.getBooleanExtra(EXTRA_UPLOAD_COMPLETE, false);
      fragment = StatisticsFragment.newInstance(dataUploadComplete);

      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.addToBackStack("statistics_activity_fragment")
          .replace(R.id.content_frame, fragment)
          .commit();
    }
  }
}
