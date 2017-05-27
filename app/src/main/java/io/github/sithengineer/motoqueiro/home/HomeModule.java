package io.github.sithengineer.motoqueiro.home;

import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;

@Module(includes = { SensorModule.class, DataModule.class }) public class HomeModule {

  private final HomeContract.View view;

  public HomeModule(HomeContract.View view) {
    this.view = view;
  }

  @Provides HomeContract.View provideView() {
    return view;
  }

  @Provides CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides HomeContract.Presenter providePresenter(HomeContract.View view, CompositeSubscriptionManager subscriptionManager,
      LocationManager locationManager, Gps.GpsStateListener locationListener, RideRepository rideRepo) {
    return new HomePresenter(view, subscriptionManager, locationManager, locationListener, rideRepo);
  }
}
