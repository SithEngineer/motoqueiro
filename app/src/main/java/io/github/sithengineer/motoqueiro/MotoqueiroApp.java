package io.github.sithengineer.motoqueiro;

import android.accounts.AccountManager;
import android.content.Context;
import io.github.sithengineer.motoqueiro.app.AppComponent;
import io.github.sithengineer.motoqueiro.app.AppModule;
import io.github.sithengineer.motoqueiro.app.DaggerAppComponent;
import io.github.sithengineer.motoqueiro.app.UiComponent;
import io.github.sithengineer.motoqueiro.data.DataCaptureComponent;
import io.github.sithengineer.motoqueiro.data.DataCaptureModule;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.data.sync.SyncComponent;
import io.github.sithengineer.motoqueiro.data.sync.SyncModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule;
import io.github.sithengineer.motoqueiro.util.ReleaseTree;
import timber.log.Timber;

public class MotoqueiroApp extends android.app.Application {

  private AppComponent appComponent;

  public static MotoqueiroApp get(Context context) {
    return (MotoqueiroApp) context.getApplicationContext();
  }

  public UiComponent createUiComponent() {
    final Context context = this;
    return appComponent.with(new DataCaptureModule(), new SensorModule(context));
  }

  public SyncComponent createSyncComponent() {
    return appComponent.with(new SyncModule());
  }

  public DataCaptureComponent createDataCaptureComponent() {
    final Context context = this;
    return appComponent.dataWith(new DataCaptureModule(), new SensorModule(context),
        new MiBandModule(context), new DataModule(context));
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }

  @Override public void onCreate() {
    super.onCreate();
    initComponents();
  }

  private void initComponents() {
    final AccountManager androidAccountManager =
        (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);

    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this, androidAccountManager, BuildConfig.MY_SECRET_KEY))
        .build();
  }

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
