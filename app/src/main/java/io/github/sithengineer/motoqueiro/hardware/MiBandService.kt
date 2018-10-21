package io.github.sithengineer.motoqueiro.hardware

import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData
import io.reactivex.Observable

interface MiBandService {
  fun listen(): Observable<MiBandData>
}
