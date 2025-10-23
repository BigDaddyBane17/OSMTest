package com.example.domain.repository

import com.example.domain.model.CameraState
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
    fun observe(): Flow<CameraState>
    suspend fun save(state: CameraState)
}