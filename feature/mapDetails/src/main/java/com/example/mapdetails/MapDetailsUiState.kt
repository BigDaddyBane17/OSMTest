package com.example.mapdetails


sealed interface MapDetailsUiState {
    data object Loading: MapDetailsUiState
    data class Content(
        val name: String,
        val lat: Double,
        val lon: Double,
        val temperature: Double?,
        val windSpeed: Double?,
    ): MapDetailsUiState
    data class Error(val message: String): MapDetailsUiState
}