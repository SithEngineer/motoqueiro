package io.github.sithengineer.motoqueiro.ui.statistics;

import dagger.Subcomponent;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;

@ActivityScope @Subcomponent(modules = { StatisticsModule.class })
public interface StatisticsComponent {
  void inject(StatisticsFragment fragment);
}
