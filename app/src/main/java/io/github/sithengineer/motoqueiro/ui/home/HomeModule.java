package io.github.sithengineer.motoqueiro.ui.home;

import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.PermissionAuthority;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;

@Module public class HomeModule {

  private final HomeContract.View view;
  private final HomeNavigator homeNavigator;
  private final PermissionAuthority permissionAuthority;

  public HomeModule(HomeContract.View view, HomeNavigator homeNavigator,
      PermissionAuthority permissionAuthority) {
    this.view = view;
    this.homeNavigator = homeNavigator;
    this.permissionAuthority = permissionAuthority;
  }

  @Provides @ActivityScope HomeContract.View provideView() {
    return view;
  }

  @Provides @ActivityScope HomeNavigator provideNavigator() {
    return homeNavigator;
  }

  @Provides @ActivityScope PermissionAuthority providePermissionAuthority() {
    return permissionAuthority;
  }

  @Provides @ActivityScope CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides @ActivityScope HomeContract.Presenter providePresenter(HomeContract.View view,
      CompositeSubscriptionManager subscriptionManager, Preferences preferences,
      HomeNavigator homeNavigator, PermissionAuthority permissionAuthority) {
    return new HomePresenter(view, subscriptionManager, preferences, homeNavigator,
        permissionAuthority);
  }
}
