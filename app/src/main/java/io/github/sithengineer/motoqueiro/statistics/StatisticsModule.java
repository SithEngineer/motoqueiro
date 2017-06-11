package io.github.sithengineer.motoqueiro.statistics;

import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;

@Module public class StatisticsModule {

  private final StatisticsContract.View view;
  private final CompositeSubscriptionManager subscriptionManager;

  public StatisticsModule(StatisticsContract.View view) {
    this(view, new CompositeSubscriptionManager());
  }

  public StatisticsModule(StatisticsContract.View view,
      CompositeSubscriptionManager subscriptionManager) {
    this.view = view;
    this.subscriptionManager = subscriptionManager;
  }

  @Provides StatisticsContract.Presenter providePresenter() {
    return new StatisticsPresenter(view, subscriptionManager);
  }
}
