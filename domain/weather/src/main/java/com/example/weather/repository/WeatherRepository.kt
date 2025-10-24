package com.example.weather.repository

import com.example.core.result.AppResult
import com.example.weather.model.Weather

interface WeatherRepository {
    suspend fun fetchCurrent(lat: Double, lon: Double): AppResult<Weather> 
}