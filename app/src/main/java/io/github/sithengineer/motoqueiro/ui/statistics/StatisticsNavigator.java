package io.github.sithengineer.motoqueiro.ui.statistics;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import io.github.sithengineer.motoqueiro.ui.home.HomeActivity;

public class StatisticsNavigator {

  private final Context context;

  public StatisticsNavigator(FragmentActivity context) {
    this.context = context;
  }

  public void navigateToHome() {
    context.startActivity(HomeActivity.getNavigationIntent(context));
  }
}
