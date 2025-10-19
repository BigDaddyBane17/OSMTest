package com.example.map

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class CameraUseCases @Inject constructor(
    val getCameraState: GetCameraState,
    val saveCameraState: SaveCameraState
)

class GetCameraState @Inject constructor(
    private val cameraStateManager: CameraStateManager
) {
    operator fun invoke(): Flow<CameraState> = cameraStateManager.cameraState
}

class SaveCameraState @Inject constructor(
    private val cameraStateManager: CameraStateManager
) {
    suspend operator fun invoke(state: CameraState) = cameraStateManager.save(state)
}
