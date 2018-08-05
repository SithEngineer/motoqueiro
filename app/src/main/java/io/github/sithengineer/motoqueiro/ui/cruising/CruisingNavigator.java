package io.github.sithengineer.motoqueiro.ui.cruising;

import android.content.Context;
import android.content.Intent;
import io.github.sithengineer.motoqueiro.data.DataCaptureService;
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsActivity;

class CruisingNavigator {

  private final Context context;

  public CruisingNavigator(Context context) {
    this.context = context;
  }

  public void goToStatistics() {
    context.startActivity(
        StatisticsActivity.getNavigationIntent(context));
  }

  public void startServiceToGatherData(String rideName) {
    Intent intent = new Intent(context, DataCaptureService.class);
    intent.putExtra(DataCaptureService.RIDE_NAME, rideName);
    context.startService(intent);
  }

  public void stopServiceToGatherData() {
    Intent intent = new Intent(DataCaptureService.STOP_SERVICE);
    context.sendBroadcast(intent);
  }
}
