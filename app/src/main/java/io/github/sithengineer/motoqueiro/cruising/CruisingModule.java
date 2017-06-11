package io.github.sithengineer.motoqueiro.cruising;

import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gps;
import io.github.sithengineer.motoqueiro.hardware.MiBand;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.BluetoothModule;
import io.github.sithengineer.motoqueiro.home.RideManager;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Named;

@Module public class CruisingModule {

  private final CruisingContract.View view;
  private final String rideId;

  public CruisingModule(CruisingContract.View view, String rideId) {
    this.view = view;
    this.rideId = rideId;
  }

  @Provides CruisingContract.View provideView() {
    return view;
  }

  @Provides @Named(CruisingActivity.EXTRA_RIDE_ID) String provideRideId() {
    return rideId;
  }

  @Provides @Named(BluetoothModule.MI_BAND_ADDRESS) String provideMiBandAddress(
      Preferences preferences) {
    return preferences.getMiBandAddressOrDefault();
  }

  @Provides CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides RideManager providesRideManager(LocationManager locationManager,
      Gps.GpsStateListener locationListener, RideRepository rideRepo) {
    return new RideManager(locationListener, locationManager, rideRepo);
  }

  @Provides DataManager providesDataManager(Accelerometer accelerometer, Gps gps,
      MiBand miBand, RideRepository rideRepo) {
    return new DataManager(accelerometer, gps, miBand, rideRepo, rideId);
  }

  @Provides CruisingContract.Presenter provideCruisingPresenter(
      CruisingContract.View view, CompositeSubscriptionManager subscriptionManager,
      DataManager dataManager, RideManager rideManager,
      @Named(CruisingActivity.EXTRA_RIDE_ID) String rideId, Preferences preferences) {
    return new CruisingPresenter(view, subscriptionManager, dataManager, rideManager,
        rideId, preferences);
  }
}
