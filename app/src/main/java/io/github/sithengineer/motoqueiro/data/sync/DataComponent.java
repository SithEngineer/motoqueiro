package io.github.sithengineer.motoqueiro.data.sync;

import android.content.AbstractThreadedSyncAdapter;
import dagger.Component;
import io.github.sithengineer.motoqueiro.data.DataModule;

@Component(modules = { DataModule.class }) interface DataComponent {
  void inject(AbstractThreadedSyncAdapter syncAdapter);
}
