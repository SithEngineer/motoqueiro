package io.github.sithengineer.motoqueiro.ui.cruising

import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.scope.ActivityScope
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager

@Module
class CruisingModule constructor(
    private val view: CruisingContract.View,
    private val cruisingNavigator: CruisingNavigator
) {

  @Provides
  @ActivityScope
  internal fun provideView(): CruisingContract.View {
    return view
  }

  @Provides
  @ActivityScope
  internal fun provideNavigator(): CruisingNavigator {
    return cruisingNavigator
  }

  @Provides
  @ActivityScope
  internal fun provideCruisingPresenter(
      view: CruisingContract.View,
      navigator: CruisingNavigator): CruisingContract.Presenter {
    return CruisingPresenter(view, CompositeSubscriptionManager(), navigator)
  }
}
