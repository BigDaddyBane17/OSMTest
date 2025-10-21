package com.example.weather.model

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("current") val current: CurrentBlock?
)

data class CurrentBlock(
    @SerializedName("time") val time: String?,
    @SerializedName("temperature_2m") val temperature: Double?,
    @SerializedName("wind_speed_10m") val windSpeed: Double?
)