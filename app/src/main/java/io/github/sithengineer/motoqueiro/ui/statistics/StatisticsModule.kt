package io.github.sithengineer.motoqueiro.ui.statistics

import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.scope.ActivityScope
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager

@Module
class StatisticsModule(
    private val view: StatisticsContract.View,
    private val navigator: StatisticsNavigator
) {

  @Provides
  @ActivityScope
  internal fun provideView(): StatisticsContract.View {
    return view
  }

  @Provides
  @ActivityScope
  internal fun provideNavigator(): StatisticsNavigator {
    return navigator
  }

  @Provides
  @ActivityScope
  internal fun providePresenter(
      view: StatisticsContract.View,
      statisticsNavigator: StatisticsNavigator): StatisticsContract.Presenter {
    return StatisticsPresenter(view, CompositeSubscriptionManager(), statisticsNavigator)
  }
}
