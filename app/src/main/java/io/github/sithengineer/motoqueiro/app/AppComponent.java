package io.github.sithengineer.motoqueiro.app;

import dagger.Component;
import io.github.sithengineer.motoqueiro.data.DataCaptureComponent;
import io.github.sithengineer.motoqueiro.data.DataCaptureModule;
import io.github.sithengineer.motoqueiro.data.sync.SyncComponent;
import io.github.sithengineer.motoqueiro.data.sync.SyncModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule;
import io.github.sithengineer.motoqueiro.network.RideApiModule;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, RideApiModule.class })
public interface AppComponent {
  UiComponent with(DataCaptureModule dataCaptureModule, SensorModule sensorModule);

  SyncComponent with(SyncModule module);

  DataCaptureComponent dataWith(DataCaptureModule module, SensorModule sensorModule,
      MiBandModule miBandModule);
}
