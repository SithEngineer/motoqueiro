package io.github.sithengineer.motoqueiro.app

import dagger.Subcomponent
import io.github.sithengineer.motoqueiro.data.DataModule
import io.github.sithengineer.motoqueiro.data.sync.SyncComponent
import io.github.sithengineer.motoqueiro.data.sync.SyncModule
import io.github.sithengineer.motoqueiro.hardware.SensorModule
import io.github.sithengineer.motoqueiro.hardware.bluetooth.BluetoothModule
import io.github.sithengineer.motoqueiro.scope.RideScope
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingComponent
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingModule
import io.github.sithengineer.motoqueiro.ui.home.HomeComponent
import io.github.sithengineer.motoqueiro.ui.home.HomeModule
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsComponent
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsModule

@RideScope
@Subcomponent(modules = [DataModule::class, SensorModule::class])
interface RideComponent {
  fun with(cruisingModule: CruisingModule, bluetoothModule: BluetoothModule): CruisingComponent

  fun with(module: HomeModule): HomeComponent

  fun with(module: StatisticsModule): StatisticsComponent

  fun with(module: SyncModule): SyncComponent
}
