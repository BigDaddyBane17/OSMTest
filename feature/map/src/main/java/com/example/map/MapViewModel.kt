package com.example.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CameraState
import com.example.domain.usecase.CameraUseCases
import com.example.point.model.Point
import com.example.point.usecase.PointUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val pointUseCases: PointUseCases,
    private val cameraUseCases: CameraUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState

    private val _effects = MutableSharedFlow<MapEffect>()
    val effects: SharedFlow<MapEffect> = _effects

    private val cameraEvents = MutableSharedFlow<CameraState>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            combine(
                pointUseCases.getAllPoints(),
                cameraUseCases.get()
            ) { pts, cam ->
                val prev = _uiState.value as? MapUiState.Success
                MapUiState.Success(
                    points          = pts,
                    selectedPointId = prev?.selectedPointId,
                    radialAnchor    = prev?.radialAnchor,
                    isMoveMode      = prev?.isMoveMode == true,
                    movePointId     = prev?.movePointId,
                    cameraLatitude  = cam.latitude,
                    cameraLongitude = cam.longitude,
                    cameraZoom      = cam.zoom
                )
            }.catch { e -> _uiState.value = MapUiState.Error(e.message ?: "Ошибка") }
                .collect { _uiState.value = it }
        }

        viewModelScope.launch {
            cameraEvents
                .debounce(100)
                .distinctUntilChangedBy { Triple(it.latitude, it.longitude, it.zoom) }
                .collect { cameraUseCases.save(it) }
        }
    }

    fun handleIntent(i: MapIntent) {
        when (i) {
            is MapIntent.AddPoint -> addPoint(i.lat, i.lon)
            is MapIntent.SelectPoint -> selectPoint(i.pointId, i.anchor)
            is MapIntent.UpdatePointPosition -> updatePointPosition(i.pointId, i.lat, i.lon)
            is MapIntent.DeletePoint -> deletePoint(i.pointId)
            is MapIntent.ClearSelection -> reduce { it.copy(selectedPointId = null, radialAnchor = null) }

            is MapIntent.StartMoveMode -> reduce { it.copy(isMoveMode = true, movePointId = i.pointId, radialAnchor = null, selectedPointId = i.pointId) }
            is MapIntent.CancelMoveMode -> reduce { it.copy(isMoveMode = false, movePointId = null) }
            is MapIntent.ApplyMove -> applyMoveToCenter(i.latitude, i.longitude)
            is MapIntent.CameraChanged -> cameraEvents.tryEmit(i.camera)

            is MapIntent.LoadWeather -> loadWeather(i.pointId)
            is MapIntent.NavigateToPointDetails -> navigateToPointDetails(i.pointId)
        }
    }

    private fun reduce(block: (MapUiState.Success) -> MapUiState.Success) {
        val cur = _uiState.value
        if (cur is MapUiState.Success) _uiState.value = block(cur)
    }

    private fun addPoint(lat: Double, lon: Double) = viewModelScope.launch {
        val p = Point(latitude = lat, longitude = lon, name = "Point ${System.currentTimeMillis()}")
        pointUseCases.insertPoint(p)
    }

    private fun selectPoint(pointId: Long, anchor: androidx.compose.ui.geometry.Offset?) {
        reduce {
            it.copy(selectedPointId = pointId, radialAnchor = anchor)
        }
    }

    private fun updatePointPosition(pointId: Long, lat: Double, lon: Double) = viewModelScope.launch {
        val s = _uiState.value as? MapUiState.Success ?: return@launch
        val p = s.points.find {
            it.id == pointId
        } ?: return@launch
        pointUseCases.updatePoint(p.copy(latitude = lat, longitude = lon))
    }

    private fun deletePoint(pointId: Long) = viewModelScope.launch {
        val s = _uiState.value as? MapUiState.Success ?: return@launch
        s.points.find { it.id == pointId }?.let { pointUseCases.deletePoint(it) }
    }

    private fun applyMoveToCenter(lat: Double, lon: Double) {
        val s = _uiState.value as? MapUiState.Success ?: return
        val id = s.movePointId ?: return
        handleIntent(MapIntent.UpdatePointPosition(id, lat, lon))
        reduce {
            it.copy(isMoveMode = false, movePointId = null)
        }
        viewModelScope.launch {
            _effects.emit(MapEffect.ShowMessage("Точка перемещена"))
        }
    }

    private fun loadWeather(pointId: Long) = viewModelScope.launch {

        val s = _uiState.value as? MapUiState.Success ?: return@launch
        val p = s.points.find { it.id == pointId } ?: return@launch
        Log.d("WEATHER", "lat=${p.latitude}, lon=${p.longitude}")
        val url = buildString {
            append("https://api.open-meteo.com/v1/forecast")
            append("?latitude="); append(p.latitude)
            append("&longitude="); append(p.longitude)
            append("&hourly=temperature_2m,relative_humidity_2m,precipitation,wind_speed_10m")
            append("&forecast_days=1")
            append("&timezone=auto")

        }

        val filename = "weather_point_${p.id}_${System.currentTimeMillis()}.json"

        _effects.emit(MapEffect.Download(url = url, filename = filename))
    }

    private fun navigateToPointDetails(pointId: Long) = viewModelScope.launch {
        _effects.emit(MapEffect.NavigateToDetails(pointId))
    }

}