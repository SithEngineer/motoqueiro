package io.github.sithengineer.motoqueiro.network;

import io.github.sithengineer.motoqueiro.data.model.RidePart;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

interface NetworkService {

  //
  // ride
  //
  @POST("ride") Observable<Void> upload(@Body RidePart ride);
}
