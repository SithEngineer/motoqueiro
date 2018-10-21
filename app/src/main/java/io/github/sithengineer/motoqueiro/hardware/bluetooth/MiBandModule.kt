package io.github.sithengineer.motoqueiro.hardware.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.text.TextUtils
import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.app.Preferences
import io.github.sithengineer.motoqueiro.hardware.MiBandDevice
import io.github.sithengineer.motoqueiro.hardware.MiBandEmpty
import io.github.sithengineer.motoqueiro.hardware.MiBandService
import io.github.sithengineer.motoqueiro.scope.RideScope
import javax.inject.Named

@Module
class MiBandModule(private val context: Context) {

  @Provides
  @RideScope
  internal fun provideBluetoothAdapter(): BluetoothAdapter {
    return (context.getSystemService(
        Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
  }

  @Provides
  @RideScope
  internal fun provideMiBand(bluetoothAdapter: BluetoothAdapter,
      @Named(MI_BAND_ADDRESS) miBandAddress: String): MiBandService {
    if (!TextUtils.isEmpty(miBandAddress)) {
      val device = bluetoothAdapter.getRemoteDevice(miBandAddress)
      return MiBandDevice(context, device)
    }
    return MiBandEmpty()
  }

  @Provides
  @RideScope
  @Named(MiBandModule.MI_BAND_ADDRESS)
  internal fun provideMiBandAddress(preferences: Preferences): String {
    return preferences.miBandAddress
  }

  companion object {
    private const val MI_BAND_ADDRESS = "mi_band_address"
  }
}
