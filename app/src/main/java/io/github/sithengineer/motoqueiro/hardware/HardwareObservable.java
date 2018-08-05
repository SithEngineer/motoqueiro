package io.github.sithengineer.motoqueiro.hardware;

import rx.Observable;

public interface HardwareObservable<C> {
  Observable<C> listen();
}
