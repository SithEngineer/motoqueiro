package io.github.sithengineer.motoqueiro.network;

import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.BuildConfig;
import io.github.sithengineer.motoqueiro.authentication.AccountManager;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
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
    return gsonBuilder.create();
  }

  @Provides @Singleton @Named("maxAgeInterceptor")
  Interceptor providesMaxAgeInterceptor() {
    return new RequestMaxAgeInterceptor();
  }

  @Provides @Singleton @Named("accountInterceptor")
  Interceptor providesAccountInterceptor(AccountManager accountManager) {
    return new AccountInfoInterceptor(accountManager);
  }

  @Provides @Singleton OkHttpClient providesHttpClient(
      @Named("maxAgeInterceptor") Interceptor maxAgeInterceptor,
      @Named("accountInterceptor") Interceptor accountInterceptor, Cache cache) {
    // set response cache
    return new OkHttpClient.Builder().addInterceptor(maxAgeInterceptor)
        .addInterceptor(accountInterceptor)
        .connectTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(4, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .cache(cache)
        .build();
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

  private static class AccountInfoInterceptor implements Interceptor {

    private final AccountManager accountManager;

    public AccountInfoInterceptor(AccountManager accountManager) {
      this.accountManager = accountManager;
    }

    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Request request = original.newBuilder()
          .header("X-User-Info", accountManager.getUserSync().getId())
          .build();

      return chain.proceed(request);
    }
  }
}
