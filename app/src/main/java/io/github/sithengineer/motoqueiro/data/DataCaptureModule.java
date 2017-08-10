package io.github.sithengineer.motoqueiro.data;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.local.RideLocalDataSource;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gravity;
import io.github.sithengineer.motoqueiro.hardware.Gyroscope;
import io.github.sithengineer.motoqueiro.hardware.MiBandService;
import io.github.sithengineer.motoqueiro.hardware.gps.Gps;
import io.github.sithengineer.motoqueiro.network.RideWebService;
import io.github.sithengineer.motoqueiro.scope.RideScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Named;

@Module public class DataCaptureModule {

  public static final String SERVICE_SUBSCRIPTIONS = "service_subscriptions";
  private final Context context;

  public DataCaptureModule(Context context) {
    this.context = context;
  }

  @Provides @RideScope RideDataSource provideLocalDataSource() {
    return new RideLocalDataSource(context);
  }

  @Provides @RideScope RideRepository provideRideRepository(RideWebService webService,
      RideDataSource localDataSource) {
    return new RideRepository(webService, localDataSource);
  }

  @Provides @RideScope DataManager providesDataManager(Accelerometer accelerometer,
      Gyroscope gyroscope, Gravity gravity, Gps gps, MiBandService miBand, RideRepository rideRepo,
      Preferences preferences) {
    return new DataManager(accelerometer, gyroscope, gravity, gps, miBand, rideRepo, preferences);
  }

  @Provides @RideScope @Named(SERVICE_SUBSCRIPTIONS)
  CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }
}
