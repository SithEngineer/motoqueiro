package io.github.sithengineer.motoqueiro.ui.cruising;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = { CruisingModule.class, MiBandModule.class })
public interface CruisingComponent {
  void inject(CruisingFragment fragment);
}
