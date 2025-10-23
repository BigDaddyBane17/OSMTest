package com.example.domain.usecase

import com.example.domain.model.CameraState
import com.example.domain.repository.CameraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class CameraUseCases @Inject constructor(
    val get: GetCameraState,
    val save: SaveCameraState
)

class GetCameraState @Inject constructor(
    private val repo: CameraRepository
) {
    operator fun invoke(): Flow<CameraState> = repo.observe()
}

class SaveCameraState @Inject constructor(
    private val repo: CameraRepository
) {
    suspend operator fun invoke(state: CameraState) = repo.save(state)
}