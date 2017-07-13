package io.github.sithengineer.motoqueiro.ui.cruising;

import android.content.Context;
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsActivity;

class CruisingNavigator {

  private final Context context;

  public CruisingNavigator(Context context) {
    this.context = context;
  }

  public void goToStatistics(boolean uploadCompleted) {
    context.startActivity(
        StatisticsActivity.getNavigationIntent(context, uploadCompleted));
  }
}
