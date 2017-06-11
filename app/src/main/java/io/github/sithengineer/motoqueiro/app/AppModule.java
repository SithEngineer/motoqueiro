package io.github.sithengineer.motoqueiro.app;

import android.app.Application;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {
  Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Application providesApplication() {
    return application;
  }

  @Provides @Singleton Preferences providesSharedPrefs() {
    return new Preferences(PreferenceManager.getDefaultSharedPreferences(application));
  }
}
