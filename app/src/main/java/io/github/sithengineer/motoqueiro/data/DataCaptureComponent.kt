package io.github.sithengineer.motoqueiro.data

import dagger.Subcomponent
import io.github.sithengineer.motoqueiro.hardware.SensorModule
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule
import io.github.sithengineer.motoqueiro.scope.RideScope

@RideScope
@Subcomponent(
    modules = [DataModule::class, DataCaptureModule::class, SensorModule::class, MiBandModule::class])
interface DataCaptureComponent {
  fun inject(dataCaptureService: DataCaptureService)
}
