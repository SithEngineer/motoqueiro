package io.github.sithengineer.motoqueiro.network;

import io.github.sithengineer.motoqueiro.data.model.Ride;
import javax.inject.Inject;
import rx.Completable;
import rx.Scheduler;

public class RideWebService {

  private final NetworkService networkService;
  private final Scheduler ioScheduler;

  @Inject public RideWebService(NetworkService networkService, Scheduler ioScheduler) {
    this.networkService = networkService;
    this.ioScheduler = ioScheduler;
  }

  public Completable upload(Ride ride) {
    return networkService.upload(ride).subscribeOn(ioScheduler).toCompletable();
  }
}
