package com.example.map

import androidx.compose.ui.geometry.Offset
import com.example.domain.model.CameraState

sealed interface MapIntent {
    data class AddPoint(val lat: Double, val lon: Double) : MapIntent
    data class SelectPoint(val pointId: Long, val anchor: Offset?) : MapIntent
    data class UpdatePointPosition(val pointId: Long, val lat: Double, val lon: Double) : MapIntent
    data class DeletePoint(val pointId: Long) : MapIntent
    data object ClearSelection : MapIntent

    data class StartMoveMode(val pointId: Long) : MapIntent
    data object CancelMoveMode : MapIntent
    data object ApplyMove : MapIntent

    data class CameraChanged(val camera: CameraState) : MapIntent

    data class LoadWeather(val pointId: Long) : MapIntent
    data class NavigateToPointDetails(val pointId: Long) : MapIntent
}

sealed interface MapEffect {
    data class NavigateToDetails(val pointId: Long) : MapEffect
    data class ShowMessage(val text: String) : MapEffect
}