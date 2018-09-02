package io.github.sithengineer.motoqueiro

import android.accounts.AccountManager
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.sithengineer.motoqueiro.app.AppComponent
import io.github.sithengineer.motoqueiro.app.AppModule
import io.github.sithengineer.motoqueiro.app.DaggerAppComponent
import io.github.sithengineer.motoqueiro.app.UiComponent
import io.github.sithengineer.motoqueiro.data.DataCaptureComponent
import io.github.sithengineer.motoqueiro.data.DataCaptureModule
import io.github.sithengineer.motoqueiro.data.DataModule
import io.github.sithengineer.motoqueiro.data.sync.SyncComponent
import io.github.sithengineer.motoqueiro.data.sync.SyncModule
import io.github.sithengineer.motoqueiro.hardware.SensorModule
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule
import io.github.sithengineer.motoqueiro.util.DebugTree
import io.github.sithengineer.motoqueiro.util.ReleaseTree
import timber.log.Timber

class MotoqueiroApp : android.app.Application() {

  private var appComponent: AppComponent? = null

  fun createUiComponent(): UiComponent {
    val context = this
    return appComponent!!.with(DataCaptureModule(), SensorModule(context))
  }

  fun createSyncComponent(): SyncComponent {
    return appComponent!!.with(SyncModule())
  }

  fun createDataCaptureComponent(): DataCaptureComponent {
    val context = this
    return appComponent!!.dataWith(DataCaptureModule(), SensorModule(context),
        MiBandModule(context), DataModule())
  }

  override fun onCreate() {
    super.onCreate()
    setupDagger()
    setupProperBackportDateClasses()
  }

  private fun setupDagger() {
    val androidAccountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

    appComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this, androidAccountManager, BuildConfig.MY_SECRET_KEY))
        .build()
  }

  private fun setupProperBackportDateClasses() {
    AndroidThreeTen.init(this)
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    setupTimber()
  }

  private fun setupTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    } else {
      Timber.plant(ReleaseTree())
    }
  }

  companion object {

    operator fun get(context: Context): MotoqueiroApp {
      return context.applicationContext as MotoqueiroApp
    }
  }
}
