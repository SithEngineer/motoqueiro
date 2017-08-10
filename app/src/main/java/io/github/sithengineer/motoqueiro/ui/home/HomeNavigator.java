package io.github.sithengineer.motoqueiro.ui.home;

import android.content.Context;
import android.content.Intent;
import io.github.sithengineer.motoqueiro.data.DataCaptureService;
import io.github.sithengineer.motoqueiro.hardware.DevicePosition;
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingActivity;

public class HomeNavigator {

  private final Context context;

  public HomeNavigator(Context context) {
    this.context = context;
  }

  public void startServiceToGatherData(String rideName, DevicePosition devicePosition){
    Intent startDataService = new Intent(context, DataCaptureService.class);
    startDataService.putExtra(DataCaptureService.RIDE_NAME, rideName);
    startDataService.putExtra(DataCaptureService.DEVICE_POSITION, devicePosition.getValue());
    context.startService(startDataService);
  }

  public void forward() {
    context.startActivity(CruisingActivity.getNavigationIntent(context));
  }
}
