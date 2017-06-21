package io.github.sithengineer.motoqueiro.home;

import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;

@Module public class HomeModule {

  private final HomeContract.View view;

  public HomeModule(HomeContract.View view) {
    this.view = view;
  }

  @Provides @ActivityScope HomeContract.View provideView() {
    return view;
  }

  @Provides @ActivityScope CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides @ActivityScope RideManager providesRideManager(LocationManager locationManager,
      Gps.GpsStateListener locationListener, RideRepository rideRepo) {
    return new RideManager(locationListener, locationManager, rideRepo);
  }

  @Provides @ActivityScope HomeContract.Presenter providePresenter(HomeContract.View view,
      CompositeSubscriptionManager subscriptionManager, RideManager rideManager,
      Preferences preferences) {
    return new HomePresenter(view, subscriptionManager, rideManager, preferences);
  }
}
