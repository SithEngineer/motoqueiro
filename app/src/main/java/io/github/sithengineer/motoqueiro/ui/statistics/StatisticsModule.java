package io.github.sithengineer.motoqueiro.ui.statistics;

import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;

@Module public class StatisticsModule {

  private final StatisticsContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;
  private final StatisticsNavigator navigator;

  public StatisticsModule(StatisticsContract.View view, StatisticsNavigator navigator) {
    this(view, new CompositeSubscriptionManager(), navigator);
  }

  public StatisticsModule(StatisticsContract.View view,
      CompositeSubscriptionManager subscriptionManager, StatisticsNavigator navigator) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
    this.navigator = navigator;
  }

  @Provides @ActivityScope StatisticsContract.View provideView() {
    return view;
  }

  @Provides @ActivityScope StatisticsNavigator provideNavigator() {
    return navigator;
  }

  @Provides @ActivityScope StatisticsContract.Presenter providePresenter(
      StatisticsContract.View view, StatisticsNavigator statisticsNavigator) {
    return new StatisticsPresenter(view, subscriptionManager, statisticsNavigator);
  }
}
