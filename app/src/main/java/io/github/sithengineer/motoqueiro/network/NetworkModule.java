package io.github.sithengineer.motoqueiro.network;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

// todo make a singleton. we only need one http client instance
@Module public class NetworkModule {

  private static final String CACHE_FILE_NAME = "responses";

  private final File cacheFile;

  public NetworkModule(File cacheFile) {
    this.cacheFile = cacheFile;
  }

  public NetworkModule(Context context) {
    this.cacheFile = new File(context.getCacheDir(), CACHE_FILE_NAME);
  }

  @Provides Retrofit provideCall() {
    // create response cache
    Cache cache = null;
    try {
      cache = new Cache(cacheFile, 10 * 1024 * 1024);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // set response cache
    OkHttpClient okHttpClient =
        new OkHttpClient.Builder().addInterceptor(new CustomHeaderInterceptor())
            .cache(cache)
            .build();

    // build retrofit client with custom serializer/de-serializer
    // scalar number conversion and Rx methods
    return new Retrofit.Builder().baseUrl(BuildConfig.BASEURL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  @Provides public NetworkService providesNetworkService(Retrofit retrofit) {
    return retrofit.create(NetworkService.class);
  }

  @Provides public RideWebService providesService(NetworkService networkService) {
    return new RideWebService(networkService);
  }

  private class CustomHeaderInterceptor implements Interceptor {
    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      // Customize the request
      Request request = original.newBuilder()
          .header("Content-Type", "application/json")
          .removeHeader("Pragma")
          .header("Cache-Control",
              String.format(Locale.ENGLISH, "max-age=%d", BuildConfig.CACHETIME))
          .build();

      okhttp3.Response response = chain.proceed(request);
      response.cacheResponse();
      // Customize or return the response
      return response;
    }
  }
}
