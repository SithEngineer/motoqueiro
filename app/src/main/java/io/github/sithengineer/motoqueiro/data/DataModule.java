package io.github.sithengineer.motoqueiro.data;

import android.accounts.Account;
import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.data.local.RideLocalDataSource;
import io.github.sithengineer.motoqueiro.data.local.User;
import io.github.sithengineer.motoqueiro.data.remote.RideRemoteDataSource;
import io.github.sithengineer.motoqueiro.network.RideWebService;
import io.github.sithengineer.motoqueiro.scope.RideScope;
import javax.inject.Named;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module public class DataModule {

  private final Context context;
  private final Account account;

  public DataModule(Context context, Account account) {
    this.context = context;
    this.account = account;
  }

  @Provides @RideScope User provideUser() {
    return new User(account.name);
  }

  @Provides Scheduler provideDefaultScheduler() {
    return Schedulers.io();
  }

  @Provides @RideScope @Named("localData") RideDataSource provideLocalDataSource(
      @NonNull Scheduler scheduler) {
    return new RideLocalDataSource(context, scheduler);
  }

  @Provides @RideScope @Named("remoteData") RideDataSource provideRemoteDataSource(
      RideWebService rideWebService) {
    return new RideRemoteDataSource(rideWebService);
  }

  @Provides @RideScope RideRepository provideRideRepository(
      @Named("localData") RideDataSource localDataSource,
      @Named("remoteData") RideDataSource remoteDataSource) {
    return new RideRepository(localDataSource, remoteDataSource);
  }
}
