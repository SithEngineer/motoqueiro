package io.github.sithengineer.motoqueiro.hardware.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.text.TextUtils
import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.hardware.MiBandDevice
import io.github.sithengineer.motoqueiro.hardware.MiBandEmpty
import io.github.sithengineer.motoqueiro.hardware.MiBandService
import io.github.sithengineer.motoqueiro.scope.ActivityScope
import javax.inject.Named

@Module
class BluetoothModule(private val context: Context) {

  @Provides
  @ActivityScope
  internal fun provideBluetoothAdapter(): BluetoothAdapter {
    return (context.getSystemService(
        Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
  }

  @Provides
  @ActivityScope
  internal fun provideMiBand(bluetoothAdapter: BluetoothAdapter,
      @Named(MI_BAND_ADDRESS) miBandAddress: String): MiBandService {
    if (!TextUtils.isEmpty(miBandAddress)) {
      val device = bluetoothAdapter.getRemoteDevice(miBandAddress)
      return MiBandDevice(context, device)
    }
    return MiBandEmpty()
  }

  companion object {
    const val MI_BAND_ADDRESS = "mi_band_address"
  }
}
