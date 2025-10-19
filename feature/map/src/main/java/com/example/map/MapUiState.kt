package com.example.map

import androidx.compose.ui.geometry.Offset
import com.example.point.Point

sealed class MapUiState {
    data object Loading : MapUiState()
    data class Success(
        val points: List<Point> = emptyList(),
        val selectedPointId: Long? = null,
        val radialAnchor: Offset? = null,
        val isMoveMode: Boolean = false,
        val movePointId: Long? = null,
        val cameraLatitude: Double = 55.7558,
        val cameraLongitude: Double = 37.6176,
        val cameraZoom: Double = 15.0
    ) : MapUiState()
    data class Error(val message: String) : MapUiState()
}