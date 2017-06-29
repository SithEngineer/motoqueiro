package io.github.sithengineer.motoqueiro.statistics;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import io.github.sithengineer.motoqueiro.home.HomeActivity;

public class StatisticsNavigator {

  private final Context context;

  public StatisticsNavigator(FragmentActivity context) {
    this.context = context;
  }

  public void navigateToHome(){
    Intent i = new Intent(context, HomeActivity.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }
}
