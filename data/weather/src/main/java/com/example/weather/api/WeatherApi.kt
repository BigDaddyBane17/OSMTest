package com.example.weather.api

import com.example.weather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getCurrent(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,wind_speed_10m",
        @Query("temperature_unit") tempUnit: String = "celsius",
        @Query("wind_speed_unit") windUnit: String = "ms",
        @Query("timezone") tz: String = "auto"
    ): WeatherResponse
}