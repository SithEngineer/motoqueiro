package io.github.sithengineer.motoqueiro.network;

import android.app.Application;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.BuildConfig;
import java.io.IOException;
import java.util.Locale;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module public class RideApiModule {

  @Provides @Singleton Cache providesRetrofitCache(Application application) {
    final int cacheSize = 10 * 1024 * 1024;
    return new Cache(application.getCacheDir(), cacheSize);
  }

  @Provides @Singleton Gson providesGsonConfiguration() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    return gsonBuilder.create();
  }

  @Provides @Singleton Interceptor providesInterceptor() {
    return new RequestMaxAgeInterceptor();
  }

  @Provides @Singleton OkHttpClient providesHttpClient(Interceptor interceptor,
      Cache cache) {
    // set response cache
    return new OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build();
  }

  @Provides @Singleton Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder().baseUrl(BuildConfig.BASEURL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  @Provides @Singleton NetworkService providesNetworkService(Retrofit retrofit) {
    return retrofit.create(NetworkService.class);
  }

  @Provides @Singleton RideWebService providesService(NetworkService networkService) {
    return new RideWebService(networkService);
  }

  private static class RequestMaxAgeInterceptor implements Interceptor {
    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Request request = original.newBuilder()
          .header("Content-Type", "application/json")
          .removeHeader("Pragma")
          .header("Cache-Control",
              String.format(Locale.ENGLISH, "max-age=%d", BuildConfig.CACHETIME))
          .build();

      okhttp3.Response response = chain.proceed(request);
      response.cacheResponse();

      return response;
    }
  }
}
