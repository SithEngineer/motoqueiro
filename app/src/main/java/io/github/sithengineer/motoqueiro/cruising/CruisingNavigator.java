package io.github.sithengineer.motoqueiro.cruising;

import android.content.Context;
import android.content.Intent;
import io.github.sithengineer.motoqueiro.statistics.StatisticsActivity;

class CruisingNavigator {

  private final Context context;

  public CruisingNavigator(Context context) {
    this.context = context;
  }

  public void goToStatistics(boolean uploadCompleted) {
    Intent i = new Intent(context, StatisticsActivity.class);
    i.putExtra(StatisticsActivity.EXTRA_UPLOAD_COMPLETE, uploadCompleted);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }
}
