package io.github.sithengineer.motoqueiro.ui.home

import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.PermissionAuthority
import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.scope.ActivityScope
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager

@Module
class HomeModule(private val view: HomeContract.View, private val homeNavigator: HomeNavigator,
    private val permissionAuthority: PermissionAuthority) {

  @Provides
  @ActivityScope
  internal fun provideView(): HomeContract.View {
    return view
  }

  @Provides
  @ActivityScope
  internal fun provideNavigator(): HomeNavigator {
    return homeNavigator
  }

  @Provides
  @ActivityScope
  internal fun providePermissionAuthority(): PermissionAuthority {
    return permissionAuthority
  }

  @Provides
  @ActivityScope
  internal fun providePresenter(view: HomeContract.View,
      preferences: Preferences, homeNavigator: HomeNavigator,
      permissionAuthority: PermissionAuthority
  ): HomeContract.Presenter {
    return HomePresenter(view, CompositeSubscriptionManager(), preferences, homeNavigator,
        permissionAuthority)
  }
}
