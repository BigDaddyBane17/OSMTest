package com.example.weather.mapper

import com.example.weather.model.Weather
import com.example.weather.model.WeatherResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun WeatherResponse.toDomain(): Weather? {
    val curr = current ?: return null
    val timeZone = timezone ?: "UTC"
    val timestamp = curr.time?.let { LocalDateTime.parse(it).atZone(ZoneId.of(timeZone)).toInstant() } ?: Instant.now()
    val temperature = curr.temperature ?: return null
    val windSpeed = curr.windSpeed ?: return null
    return Weather(temperature = temperature, windSpeed = windSpeed, timestamp = timestamp)
}