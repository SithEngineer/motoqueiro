package io.github.sithengineer.motoqueiro.app;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.data.DataCaptureModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule;
import io.github.sithengineer.motoqueiro.scope.RideScope;
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingComponent;
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingModule;
import io.github.sithengineer.motoqueiro.ui.home.HomeComponent;
import io.github.sithengineer.motoqueiro.ui.home.HomeModule;
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsComponent;
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsModule;

@RideScope @Subcomponent(modules = {
    DataCaptureModule.class, SensorModule.class
}) public interface UiComponent {
  CruisingComponent with(CruisingModule cruisingModule, MiBandModule miBandModule);

  HomeComponent with(HomeModule module);

  StatisticsComponent with(StatisticsModule module);
}
