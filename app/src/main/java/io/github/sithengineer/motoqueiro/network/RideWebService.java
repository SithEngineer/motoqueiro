package io.github.sithengineer.motoqueiro.network;

import io.github.sithengineer.motoqueiro.data.model.Ride;
import javax.inject.Inject;
import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RideWebService {

  private final NetworkService networkService;

  @Inject public RideWebService(NetworkService networkService) {
    this.networkService = networkService;
  }

  public Completable upload(Ride ride) {
    return networkService.upload(ride)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .toCompletable();
  }
}
