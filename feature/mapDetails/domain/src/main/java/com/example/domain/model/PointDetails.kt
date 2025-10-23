package com.example.domain.model

import com.example.point.model.Point
import com.example.weather.model.Weather

data class PointDetails(
    val point: Point,
    val weather: Weather?,
)