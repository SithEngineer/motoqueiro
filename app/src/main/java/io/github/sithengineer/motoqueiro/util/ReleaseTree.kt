package io.github.sithengineer.motoqueiro.util

import android.util.Log
import timber.log.Timber

class ReleaseTree : Timber.DebugTree() {

  override fun isLoggable(tag: String?, priority: Int): Boolean {
    return !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
  }
}
