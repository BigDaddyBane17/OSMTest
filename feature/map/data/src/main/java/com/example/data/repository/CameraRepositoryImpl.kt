package com.example.data.repository

import com.example.domain.model.CameraState
import com.example.domain.repository.CameraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor(
    private val local: CameraPreferencesDataSource
): CameraRepository {
    override fun observe(): Flow<CameraState> =
        local.observe().map { (lat, lon, zoom) ->
            CameraState(lat, lon, zoom)
        }


    override suspend fun save(state: CameraState) {
        local.save(state.latitude, state.longitude, state.zoom)
    }

}