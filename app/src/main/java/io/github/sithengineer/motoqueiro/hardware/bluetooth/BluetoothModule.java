package io.github.sithengineer.motoqueiro.hardware.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.hardware.MiBandDevice;
import io.github.sithengineer.motoqueiro.hardware.MiBandEmpty;
import io.github.sithengineer.motoqueiro.hardware.MiBandService;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;
import javax.inject.Named;

@Module public class BluetoothModule {

  public static final String MI_BAND_ADDRESS = "mi_band_address";

  private final Context context;

  public BluetoothModule(Context context) {
    this.context = context;
  }

  @Provides @ActivityScope BluetoothAdapter provideBluetoothAdapter() {
    return ((BluetoothManager) context.getSystemService(
        Context.BLUETOOTH_SERVICE)).getAdapter();
  }

  @Provides @ActivityScope MiBandService provideMiBand(BluetoothAdapter bluetoothAdapter,
      @Named(MI_BAND_ADDRESS) String miBandAddress) {
    if(!TextUtils.isEmpty(miBandAddress)){
      BluetoothDevice device = bluetoothAdapter.getRemoteDevice(miBandAddress);
      return new MiBandDevice(context, device);
    }
    return new MiBandEmpty();
  }
}
