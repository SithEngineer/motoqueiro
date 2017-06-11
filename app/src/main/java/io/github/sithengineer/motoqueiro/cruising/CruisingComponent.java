package io.github.sithengineer.motoqueiro.cruising;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = { CruisingModule.class })
public interface CruisingComponent {
  void inject(CruisingFragment fragment);
}
