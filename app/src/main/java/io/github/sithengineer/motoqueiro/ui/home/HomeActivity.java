package io.github.sithengineer.motoqueiro.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;

public class HomeActivity extends BaseActivity {

  public static Intent getNavigationIntent(Context context) {
    Intent intent = new Intent(context, HomeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_frame);

    HomeFragment fragment =
        (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
    if (fragment == null) {
      fragment = HomeFragment.newInstance();
      getSupportFragmentManager().beginTransaction()
          .add(R.id.content_frame, fragment)
          .commit();
    }
  }
}
