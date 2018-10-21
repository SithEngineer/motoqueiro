package io.github.sithengineer.motoqueiro.network

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.github.sithengineer.motoqueiro.BuildConfig
import io.github.sithengineer.motoqueiro.authentication.AccountManager
import io.github.sithengineer.motoqueiro.scope.ApplicationScope
import io.github.sithengineer.motoqueiro.util.VariableScrambler
import io.reactivex.Scheduler
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class RideApiModule {

  @Provides
  @ApplicationScope
  internal fun providesRetrofitCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
  }

  @Provides
  @ApplicationScope
  internal fun providesGsonConfiguration(): Gson {
    val gsonBuilder = GsonBuilder()
    return gsonBuilder.create()
  }

  @Provides
  @ApplicationScope
  @Named("maxAgeInterceptor")
  internal fun providesMaxAgeInterceptor(): Interceptor {
    return RequestMaxAgeInterceptor()
  }

  @Provides
  @ApplicationScope
  @Named("accountInterceptor")
  internal fun providesAccountInterceptor(accountManager: AccountManager,
      scrambler: VariableScrambler): Interceptor {
    return AccountInfoInterceptor(accountManager, scrambler)
  }

  @Provides
  @ApplicationScope
  internal fun providesHttpClient(
      @Named("maxAgeInterceptor") maxAgeInterceptor: Interceptor,
      @Named("accountInterceptor") accountInterceptor: Interceptor, cache: Cache): OkHttpClient {
    // set response cache
    return OkHttpClient.Builder().addInterceptor(maxAgeInterceptor)
        .addInterceptor(accountInterceptor)
        .connectTimeout(750, TimeUnit.MILLISECONDS)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .cache(cache)
        .build()
  }

  @Provides
  @ApplicationScope
  internal fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.WS_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  }

  @Provides
  @ApplicationScope
  internal fun providesNetworkService(retrofit: Retrofit): NetworkService {
    return retrofit.create(NetworkService::class.java)
  }

  @Provides
  @ApplicationScope
  internal fun providesService(networkService: NetworkService,
      ioScheduler: Scheduler): RideWebService {
    return RideWebService(networkService, ioScheduler)
  }

  private class RequestMaxAgeInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
      val original = chain.request()

      val request = original.newBuilder()
          .header("Content-Type", "application/json")
          .removeHeader("Pragma")
          .header("Cache-Control",
              String.format(Locale.ENGLISH, "max-age=%d", BuildConfig.CACHE_TIME))
          .build()

      val response = chain.proceed(request)
      response.cacheResponse()

      return response
    }
  }

  private class AccountInfoInterceptor(private val accountManager: AccountManager,
      private val scrambler: VariableScrambler) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
      val original = chain.request()

      val scrambledData = scrambler.scrambleUserId(accountManager.userSync.id.toString())

      val request = original.newBuilder()
          .header("X-User-Info-1", scrambledData.first!!)
          .header("X-User-Info-2", scrambledData.second!!)
          .build()

      return chain.proceed(request)
    }
  }
}
