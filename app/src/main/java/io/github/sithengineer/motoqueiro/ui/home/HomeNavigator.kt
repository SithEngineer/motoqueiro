package io.github.sithengineer.motoqueiro.ui.home

import android.content.Context
import android.content.Intent
import io.github.sithengineer.motoqueiro.data.DataCaptureService
import io.github.sithengineer.motoqueiro.hardware.DevicePosition
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingActivity

class HomeNavigator(private val context: Context) {

  fun startServiceToGatherData(rideName: String, devicePosition: DevicePosition) {
    val startDataService = Intent(context, DataCaptureService::class.java)
    startDataService.putExtra(DataCaptureService.RIDE_NAME, rideName)
    startDataService.putExtra(DataCaptureService.DEVICE_POSITION, devicePosition.value)
    context.startService(startDataService)
  }

  fun forward() {
    context.startActivity(CruisingActivity.getNavigationIntent(context))
  }
}
