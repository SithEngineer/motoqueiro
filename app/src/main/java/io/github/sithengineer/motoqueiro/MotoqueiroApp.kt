package io.github.sithengineer.motoqueiro

import android.accounts.AccountManager
import android.content.Context
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
import io.github.sithengineer.motoqueiro.util.ReleaseTree
import timber.log.Timber

class MotoqueiroApp : android.app.Application() {

  var appComponent: AppComponent? = null
    private set

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
    initComponents()
  }

  private fun initComponents() {
    val androidAccountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

    appComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this, androidAccountManager, BuildConfig.MY_SECRET_KEY))
        .build()
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)

    if (BuildConfig.DEBUG) {
      Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
          return "${super.createStackElementTag(element)}:$element.lineNumber"
        }
      })
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
