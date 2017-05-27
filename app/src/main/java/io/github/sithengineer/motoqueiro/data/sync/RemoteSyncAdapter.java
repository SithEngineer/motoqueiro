package io.github.sithengineer.motoqueiro.data.sync;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import io.github.sithengineer.motoqueiro.data.DataModule;
import io.github.sithengineer.motoqueiro.data.RideRepository;
import io.github.sithengineer.motoqueiro.network.NetworkModule;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import timber.log.Timber;

public class RemoteSyncAdapter extends AbstractThreadedSyncAdapter {

  private static final SimpleDateFormat formatter =
      new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS", Locale.ROOT);

  @Inject RideRepository rideRepo;

  public RemoteSyncAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    injectDependencies(context);
  }

  public RemoteSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
    super(context, autoInitialize, allowParallelSyncs);
    injectDependencies(context);
  }

  private void injectDependencies(Context context) {
    DaggerDataComponent.builder()
        .dataModule(new DataModule(context))
        .networkModule(new NetworkModule(context))
        .build()
        .inject(this);
  }

  // executed in a background thread
  @SuppressLint("RxLeakedSubscription") @Override public void onPerformSync(Account account,
      Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    rideRepo.sync().subscribe(() -> {
      Timber.i("data synced at %s", formatter.format(new Date()));
    }, err -> {
      Timber.e(err);
      Timber.e("data synced FAILED at %s", formatter.format(new Date()), err);
    });
  }
}
