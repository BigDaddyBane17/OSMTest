package com.example.weather.di


import com.example.weather.api.WeatherApi
import com.example.weather.repository.WeatherRepository
import com.example.weather.repository.WeatherRepositoryImpl
import dagger.hilt.InstallIn
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherNetworkModule {

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Provides @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides @Singleton
    fun provideRetrofit(
        okHttp: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

}