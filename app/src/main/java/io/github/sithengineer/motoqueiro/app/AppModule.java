package io.github.sithengineer.motoqueiro.app;

import android.app.Application;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.authentication.AccountManager;
import io.github.sithengineer.motoqueiro.util.VariableScrambler;
import javax.inject.Singleton;

@Module public class AppModule {
  private final Application application;
  private final android.accounts.AccountManager androidAccountManager;
  private final String mySecretKey;

  public AppModule(Application application,
      android.accounts.AccountManager accountManager, String mySecretKey) {
    this.application = application;
    this.androidAccountManager = accountManager;
    this.mySecretKey = mySecretKey;
  }

  @Provides @Singleton Application providesApplication() {
    return application;
  }

  @Provides @Singleton Preferences providesSharedPrefs() {
    return new Preferences(PreferenceManager.getDefaultSharedPreferences(application));
  }

  @Provides @Singleton VariableScrambler providesVariableScrambler() {
    return new VariableScrambler(mySecretKey);
  }

  @Provides @Singleton AccountManager providesAccountManager() {
    return new AccountManager(androidAccountManager);
  }
}
