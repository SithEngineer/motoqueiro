package io.github.sithengineer.motoqueiro.home;

import dagger.Component;

@Component(modules = { HomeModule.class }) public interface HomeComponent {
  void inject(HomeFragment fragment);
}
