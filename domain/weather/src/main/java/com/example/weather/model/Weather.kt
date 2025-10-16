package com.example.weather.model

import java.time.Instant

data class Weather(
    val temperature: Double,
    val windSpeed: Double,
    val timestamp: Instant
)