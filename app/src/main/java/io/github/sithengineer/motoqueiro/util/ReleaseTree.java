package io.github.sithengineer.motoqueiro.util;

import android.util.Log;
import timber.log.Timber;

public class ReleaseTree extends Timber.DebugTree {

  @Override protected boolean isLoggable(String tag, int priority) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
      return false;
    }
    return true;
  }
}
