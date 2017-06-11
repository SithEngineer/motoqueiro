package io.github.sithengineer.motoqueiro.app;

import dagger.Component;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.network.RideApiModule;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, RideApiModule.class })
public interface AppComponent {
  RideComponent with(DataModule dataModule, SensorModule sensorModule);
}
