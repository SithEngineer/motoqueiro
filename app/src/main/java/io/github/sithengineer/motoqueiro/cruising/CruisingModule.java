package io.github.sithengineer.motoqueiro.cruising;

import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.hardware.MiBand;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.network.NetworkModule;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Named;

@Module(includes = { SensorModule.class, NetworkModule.class, DataModule.class })
public class CruisingModule {

  private final CruisingContract.View view;
  private final String rideId;

  public CruisingModule(CruisingContract.View view, String rideId) {
    this.view = view;
    this.rideId = rideId;
  }

  @Provides CruisingContract.View provideView() {
    return view;
  }

  @Provides @Named(CruisingActivity.RIDE_ID) String provideRideId() {
    return rideId;
  }

  @Provides CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides CruisingContract.Presenter provideCruisingPresenter(CruisingContract.View view, CompositeSubscriptionManager subscriptionManager,
      Accelerometer accelerometer, Gps gps, MiBand miBand, RideRepository rideRepo,
      @Named(CruisingActivity.RIDE_ID) String rideId) {
    return new CruisingPresenter(view, subscriptionManager, accelerometer, gps, miBand, rideRepo, rideId);
  }
}
