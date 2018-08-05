package io.github.sithengineer.motoqueiro.network;

import android.app.Application;
import android.support.v4.util.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.BuildConfig;
import io.github.sithengineer.motoqueiro.authentication.AccountManager;
import io.github.sithengineer.motoqueiro.util.VariableScrambler;
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
import rx.Scheduler;

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
  Interceptor providesAccountInterceptor(AccountManager accountManager,
      VariableScrambler scrambler) {
    return new AccountInfoInterceptor(accountManager, scrambler);
  }

  @Provides @Singleton OkHttpClient providesHttpClient(
      @Named("maxAgeInterceptor") Interceptor maxAgeInterceptor,
      @Named("accountInterceptor") Interceptor accountInterceptor, Cache cache) {
    // set response cache
    return new OkHttpClient.Builder().addInterceptor(maxAgeInterceptor)
        .addInterceptor(accountInterceptor)
        .connectTimeout(750, TimeUnit.MILLISECONDS)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .cache(cache)
        .build();
  }

  @Provides @Singleton Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder().baseUrl(BuildConfig.WS_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  @Provides @Singleton NetworkService providesNetworkService(Retrofit retrofit) {
    return retrofit.create(NetworkService.class);
  }

  @Provides @Singleton RideWebService providesService(NetworkService networkService, Scheduler ioScheduler) {
    return new RideWebService(networkService, ioScheduler);
  }

  private static class RequestMaxAgeInterceptor implements Interceptor {
    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Request request = original.newBuilder()
          .header("Content-Type", "application/json")
          .removeHeader("Pragma")
          .header("Cache-Control",
              String.format(Locale.ENGLISH, "max-age=%d", BuildConfig.CACHE_TIME))
          .build();

      okhttp3.Response response = chain.proceed(request);
      response.cacheResponse();

      return response;
    }
  }

  private static class AccountInfoInterceptor implements Interceptor {

    private final AccountManager accountManager;
    private final VariableScrambler scrambler;

    public AccountInfoInterceptor(AccountManager accountManager,
        VariableScrambler scrambler) {
      this.accountManager = accountManager;
      this.scrambler = scrambler;
    }

    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Pair<String, String> scrambledData =
          scrambler.scrambleUserId(accountManager.getUserSync().getId());

      Request request = original.newBuilder()
          .header("X-User-Info-1", scrambledData.first)
          .header("X-User-Info-2", scrambledData.second)
          .build();

      return chain.proceed(request);
    }
  }
}
