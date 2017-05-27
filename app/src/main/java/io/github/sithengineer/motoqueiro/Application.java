package io.github.sithengineer.motoqueiro;

import android.content.Context;
import io.github.sithengineer.motoqueiro.util.ReleaseTree;
import timber.log.Timber;

public class Application extends android.app.Application {
  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree() {
        @Override protected String createStackElementTag(StackTraceElement element) {
          return String.format("%s:%s", super.createStackElementTag(element),
              element.getLineNumber());
        }
      });
    } else {
      Timber.plant(new ReleaseTree());
    }
  }
}
