package io.github.sithengineer.motoqueiro.ui.statistics

import android.content.Context
import android.support.v4.app.FragmentActivity
import io.github.sithengineer.motoqueiro.ui.home.HomeActivity

class StatisticsNavigator(context: FragmentActivity) {

  private val context: Context

  init {
    this.context = context
  }

  fun navigateToHome() {
    context.startActivity(HomeActivity.getNavigationIntent(context))
  }
}
