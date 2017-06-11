package io.github.sithengineer.motoqueiro;

import android.content.Context;
import io.github.sithengineer.motoqueiro.app.AppComponent;
import io.github.sithengineer.motoqueiro.app.AppModule;
import io.github.sithengineer.motoqueiro.app.DaggerAppComponent;
import io.github.sithengineer.motoqueiro.app.RideComponent;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.util.ReleaseTree;
import timber.log.Timber;

public class MotoqueiroApp extends AccountApplication {

  private AppComponent appComponent;
  private RideComponent rideComponent;

  public static MotoqueiroApp get(Context context) {
    return (MotoqueiroApp) context.getApplicationContext();
  }

  public RideComponent createDataComponent(String miBandAddress) {
    rideComponent = appComponent.with(new DataModule(this, getMainUserAccount()),
        new SensorModule(this, miBandAddress));
    return rideComponent;
  }

  public RideComponent getRideComponent() {
    return rideComponent;
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }

  public void releaseRideComponent() {
    rideComponent = null;
  }

  @Override public void onCreate() {
    super.onCreate();
    initComponents();
  }

  private void initComponents() {
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
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
