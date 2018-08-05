package io.github.sithengineer.motoqueiro.hardware;

import io.github.sithengineer.motoqueiro.hardware.capture.MiBandData;
import rx.Observable;

public interface MiBandService {
  Observable<MiBandData> listen();
}
