package io.github.sithengineer.motoqueiro.home;

import android.content.Context;
import android.content.Intent;
import io.github.sithengineer.motoqueiro.cruising.CruisingActivity;

public class HomeNavigator {

  private final Context context;

  public HomeNavigator(Context context) {
    this.context = context;
  }

  public void forward(String rideId) {
    Intent i = new Intent(context, CruisingActivity.class);
    i.putExtra(CruisingActivity.EXTRA_RIDE_ID, rideId);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }
}
