package io.github.sithengineer.motoqueiro.data;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.app.Preferences;
import io.github.sithengineer.motoqueiro.data.local.RideLocalDataSource;
import io.github.sithengineer.motoqueiro.data.remote.RideRemoteDataSource;
import io.github.sithengineer.motoqueiro.network.RideWebService;
import io.github.sithengineer.motoqueiro.scope.RideScope;
import javax.inject.Named;
import rx.Scheduler;

@Module public class DataModule {

  private static final String LOCAL_DATA = "localData";
  private static final String REMOTE_DATA = "remoteData";

  private final Context context;

  public DataModule(Context context) {
    this.context = context;
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
      @Named(REMOTE_DATA) RideDataSource remoteDataSource,
      Preferences preferences) {
    return new RideRepository(localDataSource, remoteDataSource, preferences);
  }
}
