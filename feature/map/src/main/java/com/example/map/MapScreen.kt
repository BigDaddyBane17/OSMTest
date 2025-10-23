package com.example.map

import android.graphics.Point
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.CameraState
import com.example.map.components.CenterCrosshair
import com.example.map.components.RadialMenu
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun MapScreen(
    onNavigateToDetails: (Long) -> Unit = {}
) {
    val viewmodel: MapViewModel = hiltViewModel()
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { effect ->
            when (effect) {
                is MapEffect.NavigateToDetails -> onNavigateToDetails(effect.pointId)
                is MapEffect.ShowMessage -> launch {
                    snack.showSnackbar(effect.text, duration = SnackbarDuration.Short)
                }
            }
        }
    }


    Scaffold(snackbarHost = { SnackbarHost(snack) }) { padd ->
        when (val s = state) {
            MapUiState.Loading -> Box(Modifier.fillMaxSize().padding(padd), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is MapUiState.Error -> Box(Modifier.fillMaxSize().padding(padd), contentAlignment = Alignment.Center) {
                Text("Ошибка: ${s.message}", color = MaterialTheme.colorScheme.error)
            }
            is MapUiState.Success -> MapContent(s, viewmodel::handleIntent, Modifier.padding(padd))
        }
    }
}

@Composable
private fun MapContent(
    s: MapUiState.Success,
    onIntent: (MapIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var boxOffset by remember { mutableStateOf(IntOffset.Zero) }
    var appliedInitialCamera by remember { mutableStateOf(false) }
    val isMoveModeState = rememberUpdatedState(s.isMoveMode)

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    DisposableEffect(lifecycle, mapView) {
        val obs = LifecycleEventObserver { _, e ->
            when (e) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE  -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycle.addObserver(obs)
        onDispose {
            lifecycle.removeObserver(obs)
            mapView.onDetach()
        }
    }



    LaunchedEffect(mapView) {
        mapView.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                val c = mapView.mapCenter
                onIntent(MapIntent.CameraChanged(
                    CameraState(
                        c.latitude,
                        c.longitude,
                        mapView.zoomLevelDouble
                    )
                ))
                return false
            }


            override fun onZoom(event: ZoomEvent?): Boolean {
                val c = mapView.mapCenter
                onIntent(MapIntent.CameraChanged(
                    CameraState(
                        c.latitude,
                        c.longitude,
                        mapView.zoomLevelDouble
                    )
                ))
                return false
            }
        })
    }

    Box(Modifier.fillMaxSize().then(modifier)) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    boxOffset = IntOffset(
                        it.positionInWindow().x.toInt(),
                        it.positionInWindow().y.toInt()
                    )
                },
            factory = { mapView },
            update = { mv ->
                if (!appliedInitialCamera) {
                    mv.controller.setZoom(s.cameraZoom)
                    mv.controller.setCenter(GeoPoint(s.cameraLatitude, s.cameraLongitude))
                    appliedInitialCamera = true

                }

                if (mv.overlays.none { it is MapEventsOverlay }) {
                    mv.overlays.add(
                        MapEventsOverlay(object : MapEventsReceiver {
                            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                                if (isMoveModeState.value) return true
                                p?.let { onIntent(MapIntent.AddPoint(it.latitude, it.longitude)) }
                                return true
                            }
                            override fun longPressHelper(p: GeoPoint?) = false
                        })
                    )
                }

                mv.overlays.removeAll { it is Marker }
                s.points.forEach { point ->
                    val marker = Marker(mv).apply {
                        position = GeoPoint(point.latitude, point.longitude)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        relatedObject = point.id
                        setOnMarkerClickListener { m, _ ->
                            if (isMoveModeState.value) return@setOnMarkerClickListener true
                            val id = m.relatedObject as? Long ?: point.id
                            val scr = Point().also { mv.projection.toPixels(m.position, it) }
                            val anchor = Offset(
                                (scr.x - boxOffset.x).toFloat(),
                                (scr.y - boxOffset.y).toFloat()
                            )
                            onIntent(MapIntent.SelectPoint(id, anchor))
                            true
                        }
                    }
                    mv.overlays.add(marker)
                }
                mv.invalidate()
            }
        )

        if (s.selectedPointId != null && s.radialAnchor != null && !s.isMoveMode) {
            RadialMenu(
                center = s.radialAnchor,
                onWeather = { onIntent(MapIntent.LoadWeather(s.selectedPointId)) },
                onInfo    = { onIntent(MapIntent.NavigateToPointDetails(s.selectedPointId)) },
                onMove    = { onIntent(MapIntent.StartMoveMode(s.selectedPointId)) },
                onDismiss = { onIntent(MapIntent.ClearSelection) }
            )
        }

        if (s.isMoveMode) {
            CenterCrosshair()

            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onIntent(MapIntent.ApplyMove) },
                    modifier = Modifier.weight(1f)
                ) { Text("Применить") }

                OutlinedButton(
                    onClick = { onIntent(MapIntent.CancelMoveMode) },
                    modifier = Modifier.weight(1f)
                ) { Text("Отменить") }
            }
        }
    }
}


