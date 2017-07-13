package io.github.sithengineer.motoqueiro.ui.home;

import android.content.Context;
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingActivity;

public class HomeNavigator {

  private final Context context;

  public HomeNavigator(Context context) {
    this.context = context;
  }

  public void forward(String rideId) {
    context.startActivity(CruisingActivity.getNavigationIntent(context, rideId));
  }
}
