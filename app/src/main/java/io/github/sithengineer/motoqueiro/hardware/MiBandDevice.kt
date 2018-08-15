package io.github.sithengineer.motoqueiro.hardware

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import io.github.sithengineer.motoqueiro.hardware.bluetooth.RxBluetoothGattCallback
import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData
import io.reactivex.Observable
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit

class MiBandDevice(private val context: Context, private val miBandDevice: BluetoothDevice) :
    HardwareObservable<MiBandData>, MiBandService {

  private val callBack: RxBluetoothGattCallback = RxBluetoothGattCallback()

  private var myGatBand: BluetoothGatt? = null

  override fun listen(): Observable<MiBandData> {

    val getMiBandFromCallback = callBack.listen()
        .filter { event -> event.characteristic != null }
        .map { gattEvent ->
          val characteristic = gattEvent.characteristic
          val alertUUID = characteristic!!.uuid

          return@map when (alertUUID) {
            MiBandConstants.UUID_NOTIFICATION_HEARTRATE -> {
              val heartBeat = characteristic.value[1]
              MiBandData(heartBeat.toInt())
            }
            MiBandConstants.UUID_BUTTON_TOUCH -> MiBandData(true)
            else -> MiBandData()
          }
        }

    return writeHeartBeatRequestInterval().publish()
        .refCount()
        .flatMap { _ -> getMiBandFromCallback }
        .doOnNext { data -> Timber.v("MiBand: %s", data.toString()) }
  }

  private fun writeHeartBeatRequestInterval(): Observable<Void> {
    val setupNotificationsObservable = Observable.timer(5, TimeUnit.SECONDS).map<Void> {
      getNotifications(MiBandConstants.UUID_SERVICE_MIBAND2_SERVICE,
          MiBandConstants.UUID_BUTTON_TOUCH)
      Timber.d("setup to get touch notifications")
      null
    }

    val setupHeartRateObservable = Observable.timer(5, TimeUnit.SECONDS).map<Void> {
      getNotificationsWithDescriptor(MiBandConstants.UUID_SERVICE_HEARTBEAT,
          MiBandConstants.UUID_NOTIFICATION_HEARTRATE,
          MiBandConstants.UUID_DESCRIPTOR_UPDATE_NOTIFICATION)
      Timber.d("setup to get heart rate data")
      null
    }

    val sendHearRateReadRequests = Observable.interval(15, TimeUnit.SECONDS).map<Void> {
      writeData(MiBandConstants.UUID_SERVICE_HEARTBEAT,
          MiBandConstants.UUID_START_HEARTRATE_CONTROL_POINT,
          MiBandConstants.BYTE_NEW_HEART_RATE_SCAN)
      null
    }

    return setupNotificationsObservable
        .concatWith(setupHeartRateObservable)
        .concatWith(sendHearRateReadRequests)
        .doOnSubscribe { connect() }
        .doFinally { disconnect() }
  }

  private fun connect() {
    myGatBand = miBandDevice.connectGatt(context, true, callBack)
    Timber.i("connected")
  }

  private fun getNotifications(service: UUID, characteristic: UUID) {
    if (myGatBand == null) {
      Timber.w("can't get notifications") // not initialized?
      return
    }

    val bluetoothGattService = myGatBand!!.getService(service)
    if (bluetoothGattService != null) {

      val myGatChar = bluetoothGattService.getCharacteristic(characteristic)
      if (myGatChar != null) {
        myGatBand!!.setCharacteristicNotification(myGatChar, true)
      }
    }
  }

  /**
   * Get notification but also set descriptor to Enable notification. You need to wait
   * couple of
   * seconds before you could use it (at least in the mi band 2)
   */
  private fun getNotificationsWithDescriptor(service: UUID, Characteristics: UUID,
      Descriptor: UUID) {
    if (myGatBand == null) {
      Timber.w("can't get notifications") // not initialized?
      return
    }

    val myGatService = myGatBand!!.getService(service)
    if (myGatService != null) {

      val myGatChar = myGatService.getCharacteristic(Characteristics)
      if (myGatChar != null) {

        myGatBand!!.setCharacteristicNotification(myGatChar, true)

        val myDescriptor = myGatChar.getDescriptor(Descriptor)
        if (myDescriptor != null) {
          myDescriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
          myGatBand!!.writeDescriptor(myDescriptor)
        }
      }
    }
  }

  private fun disconnect() {
    myGatBand!!.disconnect()
    myGatBand!!.close()
    myGatBand = null
    Timber.i("disconnected")
  }

  private fun writeData(service: UUID, characteristic: UUID, data: ByteArray) {
    if (myGatBand == null) {
      Timber.w("can't read data") // not initialized maybe?
      return
    }

    val myGatService = myGatBand!!.getService(service)
    if (myGatService != null) {
      val myGatChar = myGatService.getCharacteristic(characteristic)
      if (myGatChar != null) {
        myGatChar.value = data
        myGatBand!!.writeCharacteristic(myGatChar)
      }
    }
  }
}
