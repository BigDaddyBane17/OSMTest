package com.example.domain.usecase

import com.example.core.result.AppResult
import com.example.domain.model.PointDetails
import com.example.point.repository.PointRepository
import com.example.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObservePointDetails @Inject constructor(
    private val points: PointRepository,
    private val weather: WeatherRepository
) {
    operator fun invoke(pointId: Long): Flow<PointDetails> =
        flow {
            val p = points.getPointById(pointId) ?: error("Точка не найдена: $pointId")
            emit(PointDetails(point = p, weather = null))

            when (val r = weather.fetchCurrent(p.latitude, p.longitude)) {
                is AppResult.Success -> emit(PointDetails(point = p, weather = r.value))
                is AppResult.Error   -> {
                    emit(PointDetails(point = p, weather = null))
                }
            }
        }.flowOn(kotlinx.coroutines.Dispatchers.IO)
}