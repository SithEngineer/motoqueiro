package io.github.sithengineer.motoqueiro.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;

@Module public class AppModule {
  Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides Application providesApplication() {
    return application;
  }

  @Provides SharedPreferences providesSharedPrefs() {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }
}
