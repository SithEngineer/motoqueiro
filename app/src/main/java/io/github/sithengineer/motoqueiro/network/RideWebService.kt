package io.github.sithengineer.motoqueiro.network

import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Inject

class RideWebService @Inject constructor(private val networkService: NetworkService,
    private val ioScheduler: Scheduler) {

  fun upload(ride: Ride): Completable {
    return networkService.upload(ride).subscribeOn(ioScheduler).ignoreElements()
  }
}
