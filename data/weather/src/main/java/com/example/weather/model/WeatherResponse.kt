package com.example.weather.model


data class WeatherResponse(
    val timezone: String?,
    val current: CurrentBlock?
)

data class CurrentBlock(
    val time: String?,
    val temperature: Double?,
    val windSpeed: Double?
)