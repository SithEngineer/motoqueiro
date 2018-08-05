package io.github.sithengineer.motoqueiro.hardware;

import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import rx.Observable;

public class MiBandEmpty implements HardwareObservable<MiBandData>, MiBandService {
  @Override public Observable<MiBandData> listen() {
    return Observable.empty();
  }
}
