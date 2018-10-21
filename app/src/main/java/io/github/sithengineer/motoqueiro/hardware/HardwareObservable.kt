package io.github.sithengineer.motoqueiro.hardware

import io.reactivex.Observable

interface HardwareObservable<C> {
  fun listen(): Observable<C>
}
