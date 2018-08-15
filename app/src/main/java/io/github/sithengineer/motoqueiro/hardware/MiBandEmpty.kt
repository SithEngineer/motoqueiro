package io.github.sithengineer.motoqueiro.hardware

import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData
import io.reactivex.Observable

class MiBandEmpty : HardwareObservable<MiBandData>, MiBandService {
  override fun listen(): Observable<MiBandData> {
    return Observable.empty()
  }
}
