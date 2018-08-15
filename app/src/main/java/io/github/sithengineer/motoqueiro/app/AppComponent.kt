package io.github.sithengineer.motoqueiro.app

import dagger.Component
import io.github.sithengineer.motoqueiro.data.DataCaptureComponent
import io.github.sithengineer.motoqueiro.data.DataCaptureModule
import io.github.sithengineer.motoqueiro.data.DataModule
import io.github.sithengineer.motoqueiro.data.sync.SyncComponent
import io.github.sithengineer.motoqueiro.data.sync.SyncModule
import io.github.sithengineer.motoqueiro.hardware.SensorModule
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule
import io.github.sithengineer.motoqueiro.network.RideApiModule
import io.github.sithengineer.motoqueiro.scope.ApplicationScope

@ApplicationScope
@Component(modules = [AppModule::class, RideApiModule::class])
interface AppComponent {
  fun with(dataCaptureModule: DataCaptureModule, sensorModule: SensorModule): UiComponent

  fun with(module: SyncModule): SyncComponent

  fun dataWith(module: DataCaptureModule, sensorModule: SensorModule,
      miBandModule: MiBandModule, dataModule: DataModule): DataCaptureComponent
}
