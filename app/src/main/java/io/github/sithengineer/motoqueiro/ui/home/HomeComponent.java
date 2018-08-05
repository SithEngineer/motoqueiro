package io.github.sithengineer.motoqueiro.ui.home;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = { HomeModule.class })
public interface HomeComponent {
  void inject(HomeFragment fragment);
}
