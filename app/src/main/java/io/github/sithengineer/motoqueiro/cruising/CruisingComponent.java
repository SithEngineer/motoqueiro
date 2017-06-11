package io.github.sithengineer.motoqueiro.cruising;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.BluetoothModule;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = { CruisingModule.class, BluetoothModule.class })
public interface CruisingComponent {
  void inject(CruisingFragment fragment);
}
