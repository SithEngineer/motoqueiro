package io.github.sithengineer.motoqueiro.statistics;

import dagger.Component;

@Component(modules = { StatisticsModule.class }) public interface StatisticsComponent {
  void inject(StatisticsFragment fragment);
}
