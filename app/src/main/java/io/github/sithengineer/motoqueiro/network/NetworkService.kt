package io.github.sithengineer.motoqueiro.network

import io.github.sithengineer.motoqueiro.data.local.entity.Ride
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {

  @POST("ride")
  fun upload(@Body ride: Ride): Observable<Void>
}
