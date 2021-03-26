package com.alansoft.kacote.di

import com.alansoft.kacote.data.KakaoSearchDataSource
import com.alansoft.kacote.data.api.KakaoSearchApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        @Provides
        fun providesOkHttpClient(): OkHttpClient {
            val chainInterceptor = { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "KakaoAK 0e4079250a010fd99eb6e56583f42202")
                        .build()
                )
            }
            return OkHttpClient.Builder()
                .addInterceptor(chainInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        fun providesRetrofitBuilder(client: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
//                            .setDateFormat("[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]")
                            .create()
                    )
                )
                .client(client)
                .build()

        @Provides
        fun providesKakaoSearchApi(retrofit: Retrofit): KakaoSearchApi =
            retrofit.create(KakaoSearchApi::class.java)

        @Provides
        fun provideCharacterRemoteDataSource(kakaoSearchApi: KakaoSearchApi) =
            KakaoSearchDataSource(kakaoSearchApi)

    }
}