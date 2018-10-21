package io.github.sithengineer.motoqueiro.app

import android.app.Application
import android.arch.persistence.room.Room
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.authentication.AccountManager
import io.github.sithengineer.motoqueiro.data.local.AppDatabase
import io.github.sithengineer.motoqueiro.scope.ApplicationScope
import io.github.sithengineer.motoqueiro.util.VariableScrambler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
class AppModule(private val application: Application,
    private val androidAccountManager: android.accounts.AccountManager,
    private val mySecretKey: String) {

  @Provides
  @ApplicationScope
  internal fun providesApplication(): Application {
    return application
  }

  @Provides
  @ApplicationScope
  internal fun providesIoScheduler(): Scheduler {
    return Schedulers.io()
  }

  @Provides
  @ApplicationScope
  internal fun providesSharedPrefs(): Preferences {
    return Preferences(PreferenceManager.getDefaultSharedPreferences(application))
  }

  @Provides
  @ApplicationScope
  internal fun providesVariableScrambler(): VariableScrambler {
    return VariableScrambler(mySecretKey)
  }

  @Provides
  @ApplicationScope
  internal fun providesAccountManager(): AccountManager {
    return AccountManager(androidAccountManager)
  }

  @Provides
  @ApplicationScope
  internal fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application.applicationContext, AppDatabase::class.java,
        "motoq-db").build()
  }

}
