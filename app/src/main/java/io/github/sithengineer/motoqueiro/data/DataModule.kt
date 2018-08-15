package io.github.sithengineer.motoqueiro.data

import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.data.local.AppDatabase
import io.github.sithengineer.motoqueiro.data.local.RideLocalDataSource
import io.github.sithengineer.motoqueiro.data.remote.RideRemoteDataSource
import io.github.sithengineer.motoqueiro.network.RideWebService
import io.github.sithengineer.motoqueiro.scope.RideScope
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class DataModule {

  @Provides
  @RideScope
  @Named(LOCAL_DATA)
  internal fun provideLocalDataSource(
      appDatabase: AppDatabase,
      scheduler: Scheduler): RideDataSource {
    return RideLocalDataSource(appDatabase, scheduler)
  }

  @Provides
  @RideScope
  @Named(REMOTE_DATA)
  internal fun provideRemoteDataSource(
      rideWebService: RideWebService): RideDataSource {
    return RideRemoteDataSource(rideWebService)
  }

  @Provides
  @RideScope
  internal fun provideRideRepository(
      @Named(LOCAL_DATA) localDataSource: RideDataSource,
      @Named(REMOTE_DATA) remoteDataSource: RideDataSource,
      preferences: Preferences): RideRepository {
    return RideRepository(localDataSource, remoteDataSource, preferences)
  }

  companion object {
    private const val LOCAL_DATA = "localData"
    private const val REMOTE_DATA = "remoteData"
  }
}
