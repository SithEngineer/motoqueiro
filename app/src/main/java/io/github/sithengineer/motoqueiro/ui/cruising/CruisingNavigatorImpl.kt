package io.github.sithengineer.motoqueiro.ui.cruising

import android.content.Context
import android.content.Intent
import io.github.sithengineer.motoqueiro.data.DataCaptureService
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsActivity

class CruisingNavigatorImpl(private val context: Context) : CruisingNavigator {

  override fun goToStatistics() {
    context.startActivity(
        StatisticsActivity.getNavigationIntent(context))
  }

  override fun startServiceToGatherData(rideName: String) {
    val intent = Intent(context, DataCaptureService::class.java)
    intent.putExtra(DataCaptureService.RIDE_NAME, rideName)
    context.startService(intent)
  }

  override fun stopServiceToGatherData() {
    val intent = Intent(DataCaptureService.STOP_SERVICE)
    context.sendBroadcast(intent)
  }
}
