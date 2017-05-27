package io.github.sithengineer.motoqueiro.home;

import android.os.Bundle;
import io.github.sithengineer.motoqueiro.BaseActivity;
import io.github.sithengineer.motoqueiro.R;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.hardware.SensorModule;
import io.github.sithengineer.motoqueiro.network.NetworkModule;

public class HomeActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_frame);

    HomeFragment fragment =
        (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
    if (fragment == null) {
      fragment = HomeFragment.newInstance();
      getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();
    }

    DaggerHomeComponent.builder()
        .sensorModule(new SensorModule(this))
        .homeModule(new HomeModule(fragment))
        .dataModule(new DataModule(this))
        .networkModule(new NetworkModule(this))
        .build()
        .inject(fragment);
  }
}
