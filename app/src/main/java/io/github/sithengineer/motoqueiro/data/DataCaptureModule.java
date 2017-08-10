package io.github.sithengineer.motoqueiro.data;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.local.RideLocalDataSource;
import io.github.sithengineer.motoqueiro.data.remote.RideRemoteDataSource;
import io.github.sithengineer.motoqueiro.hardware.Accelerometer;
import io.github.sithengineer.motoqueiro.hardware.Gravity;
import io.github.sithengineer.motoqueiro.hardware.Gyroscope;
import io.github.sithengineer.motoqueiro.hardware.MiBandService;
import io.github.sithengineer.motoqueiro.hardware.gps.Gps;
import io.github.sithengineer.motoqueiro.network.RideWebService;
import io.github.sithengineer.motoqueiro.scope.RideScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;
import javax.inject.Named;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module public class DataCaptureModule {

  public static final String SERVICE_SUBSCRIPTIONS = "service_subscriptions";
  private static final String LOCAL_DATA = "local_data";
  private static final String REMOTE_DATA = "remote_data";
  private final Context context;

  public DataCaptureModule(Context context) {
    this.context = context;
  }

  @Provides Scheduler provideDefaultScheduler() {
    return Schedulers.io();
  }

  @Provides @RideScope @Named(LOCAL_DATA) RideDataSource provideLocalDataSource(
      @NonNull Scheduler scheduler) {
    return new RideLocalDataSource(context, scheduler);
  }

  @Provides @RideScope @Named(REMOTE_DATA) RideDataSource provideRemoteDataSource(
      RideWebService rideWebService) {
    return new RideRemoteDataSource(rideWebService);
  }

  @Provides @RideScope RideRepository provideRideRepository(
      @Named(LOCAL_DATA) RideDataSource localDataSource,
      @Named(REMOTE_DATA) RideDataSource remoteDataSource) {
    return new RideRepository(localDataSource, remoteDataSource);
  }

  @Provides @RideScope DataManager providesDataManager(Accelerometer accelerometer,
      Gyroscope gyroscope, Gravity gravity, Gps gps, MiBandService miBand, RideRepository rideRepo,
      Preferences preferences) {
    return new DataManager(accelerometer, gyroscope, gravity, gps, miBand, rideRepo, preferences);
  }

  @Provides @RideScope DataCaptureService.StopSignalReceiver providesStopSignalReceiver() {
    return new DataCaptureService.StopSignalReceiver();
  }

  @Provides @RideScope @Named(SERVICE_SUBSCRIPTIONS)
  CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }
}
