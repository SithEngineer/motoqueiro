package io.github.sithengineer.motoqueiro.data.sync

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import io.github.sithengineer.motoqueiro.MotoqueiroApp
import io.github.sithengineer.motoqueiro.data.RideRepository
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RemoteSyncAdapter : AbstractThreadedSyncAdapter {

  @Inject
  lateinit var rideRepo: RideRepository

  constructor(context: Context, autoInitialize: Boolean) : super(context, autoInitialize) {
    injectDependencies(context)
  }

  constructor(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean) : super(
      context, autoInitialize, allowParallelSyncs) {
    injectDependencies(context)
  }

  private fun injectDependencies(context: Context) {
    MotoqueiroApp[context].createSyncComponent().inject(this)
  }

  // executed in a background thread
  @SuppressLint("RxLeakedSubscription")
  override fun onPerformSync(account: Account,
      extras: Bundle, authority: String, provider: ContentProviderClient, syncResult: SyncResult) {

    rideRepo.sync().subscribe({ Timber.i("data synced at %s", formatter.format(Date())) },
        { err ->
          Timber.e(err, "data synced FAILED at ${formatter.format(Date())}")
        })
  }

  companion object {
    private val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS", Locale.getDefault())
  }
}
