package io.github.sithengineer.motoqueiro.ui.cruising

interface CruisingNavigator {
  fun stopServiceToGatherData()
  fun startServiceToGatherData(rideName: String)
  fun goToStatistics()
}
