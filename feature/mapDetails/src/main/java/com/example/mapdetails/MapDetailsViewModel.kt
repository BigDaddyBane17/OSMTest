package com.example.mapdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.PointDetails
import com.example.domain.usecase.ObservePointDetails
import com.example.point.PointRepository
import com.example.weather.repository.WeatherRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn



@HiltViewModel
class MapDetailsViewModel @Inject constructor(
    observePointDetails: ObservePointDetails,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pointId: Long = checkNotNull(savedStateHandle.get<Long>("pointId"))

    val uiState: StateFlow<MapDetailsUiState> =
        observePointDetails(pointId)
            .map<PointDetails, MapDetailsUiState> { d ->
                MapDetailsUiState.Content(
                    name = d.point.name.orEmpty(),
                    lat = d.point.latitude,
                    lon = d.point.longitude,
                    temperature = d.weather?.temperature,
                    windSpeed = d.weather?.windSpeed
                )
            }
            .onStart {
                emit(MapDetailsUiState.Loading)
            }
            .catch   { e ->
                emit(MapDetailsUiState.Error(e.message ?: "Ошибка"))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                MapDetailsUiState.Loading
            )
}
