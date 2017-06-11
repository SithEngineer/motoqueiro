package io.github.sithengineer.motoqueiro.statistics;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = { StatisticsModule.class })
public interface StatisticsComponent {
  void inject(StatisticsFragment fragment);
}
