package com.example.weather.repository

import com.example.core.result.AppResult
import com.example.core.result.runCatchingApp
import com.example.weather.api.WeatherApi
import com.example.weather.mapper.toDomain
import com.example.weather.model.Weather
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {
    override suspend fun fetchCurrent(
        lat: Double,
        lon: Double
    ): AppResult<Weather> =
        runCatchingApp {
            val dto = api.getCurrent(lat, lon)
            dto.toDomain() ?: error("Empty block")
        }
}