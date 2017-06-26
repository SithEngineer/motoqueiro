package io.github.sithengineer.motoqueiro.hardware;

import rx.Observable;

interface HardwareObservable<C> {
  Observable<C> listen();
}
