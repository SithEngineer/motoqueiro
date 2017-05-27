package io.github.sithengineer.motoqueiro.data;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.data.local.RideLocalDataSource;
import io.github.sithengineer.motoqueiro.data.remote.RideRemoteDataSource;
import io.github.sithengineer.motoqueiro.network.NetworkModule;
import io.github.sithengineer.motoqueiro.network.RideWebService;
import javax.inject.Named;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module(includes = { NetworkModule.class }) public class DataModule {

  private final Context context;

  public DataModule(Context context) {
    this.context = context;
  }

  @Provides public Scheduler provideDefaultScheduler() {
    return Schedulers.io();
  }

  @Provides @Named("localData")
  public RideDataSource provideLocalDataSource(@NonNull Scheduler scheduler) {
    return new RideLocalDataSource(context, scheduler);
  }

  @Provides @Named("remoteData")
  public RideDataSource provideRemoteDataSource(@NonNull RideWebService rideWebService) {
    return new RideRemoteDataSource(rideWebService);
  }

  @Provides
  public RideRepository provideRideRepository(@Named("localData") RideDataSource localDataSource,
      @Named("remoteData") RideDataSource remoteDataSource) {
    return new RideRepository(localDataSource, remoteDataSource);
  }
}
