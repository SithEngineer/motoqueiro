package io.github.sithengineer.motoqueiro.cruising;

import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.MiBandService;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.BluetoothModule;
import io.github.sithengineer.motoqueiro.hardware.gps.Gps;
import io.github.sithengineer.motoqueiro.hardware.gps.GpsStateListener;
import io.github.sithengineer.motoqueiro.home.RideManager;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Named;

@Module public class CruisingModule {

  private final CruisingContract.View view;
  private final CruisingNavigator cruisingNavigator;
  private final String rideId;

  public CruisingModule(CruisingContract.View view, String rideId, CruisingNavigator cruisingNavigator) {
    this.view = view;
    this.rideId = rideId;
    this.cruisingNavigator = cruisingNavigator;
  }

  @Provides @ActivityScope CruisingContract.View provideView() {
    return view;
  }

  @Provides @ActivityScope CruisingNavigator provideNavigator() {
    return cruisingNavigator;
  }

  @Provides @ActivityScope @Named(CruisingActivity.EXTRA_RIDE_ID) String provideRideId() {
    return rideId;
  }

  @Provides @ActivityScope @Named(BluetoothModule.MI_BAND_ADDRESS) String provideMiBandAddress(Preferences preferences) {
    return preferences.getMiBandAddressOrDefault();
  }

  @Provides @ActivityScope CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides @ActivityScope RideManager providesRideManager(LocationManager locationManager, GpsStateListener locationListener,
      RideRepository rideRepo) {
    return new RideManager(locationListener, locationManager, rideRepo);
  }

  @Provides @ActivityScope DataManager providesDataManager(Accelerometer accelerometer, Gps gps, MiBandService miBand,
      RideRepository rideRepo) {
    return new DataManager(accelerometer, gps, miBand, rideRepo, rideId);
  }

  @Provides @ActivityScope CruisingContract.Presenter provideCruisingPresenter(CruisingContract.View view,
      CompositeSubscriptionManager subscriptionManager, DataManager dataManager, RideManager rideManager,
      @Named(CruisingActivity.EXTRA_RIDE_ID) String rideId, CruisingNavigator navigator) {
    return new CruisingPresenter(view, subscriptionManager, dataManager, rideManager, rideId, navigator);
  }
}
