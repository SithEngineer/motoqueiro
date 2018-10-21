package io.github.sithengineer.motoqueiro.data.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

class RemoteSyncService : Service() {

  private val _lock = Any()

  override fun onCreate() {
    super.onCreate()
    synchronized(_lock) {
      if (adapter == null) {
        adapter = RemoteSyncAdapter(applicationContext, true, false)
      }
    }
  }

  override fun onBind(intent: Intent): IBinder? {
    return adapter!!.syncAdapterBinder
  }

  companion object {
    private var adapter: RemoteSyncAdapter? = null
  }
}
