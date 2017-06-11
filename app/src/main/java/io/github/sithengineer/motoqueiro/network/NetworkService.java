package io.github.sithengineer.motoqueiro.network;

import io.github.sithengineer.motoqueiro.data.model.RidePart;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface NetworkService {

  //
  // ride
  //
  @POST("ride") Observable<Void> upload(@Body RidePart ride);
}
