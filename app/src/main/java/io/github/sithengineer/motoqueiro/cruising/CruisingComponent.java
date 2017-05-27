package io.github.sithengineer.motoqueiro.cruising;

import dagger.Component;

@Component(modules = { CruisingModule.class }) public interface CruisingComponent {
  void inject(CruisingFragment fragment);
}
