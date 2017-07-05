package io.github.sithengineer.motoqueiro.home;

import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.PermissionAuthority;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener;
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

  @Provides @ActivityScope
  CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides @ActivityScope RideManager providesRideManager(
      LocationManager locationManager, GpsStateListener locationListener,
      RideRepository rideRepo) {
    return new RideManager(locationListener, locationManager, rideRepo);
  }

  @Provides @ActivityScope HomeContract.Presenter providePresenter(HomeContract.View view,
      CompositeSubscriptionManager subscriptionManager, RideManager rideManager,
      Preferences preferences, HomeNavigator homeNavigator,
      PermissionAuthority permissionAuthority) {
    return new HomePresenter(view, subscriptionManager, rideManager, preferences,
        homeNavigator, permissionAuthority);
  }
}
