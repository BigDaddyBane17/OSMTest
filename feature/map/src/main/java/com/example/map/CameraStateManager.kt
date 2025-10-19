package com.example.map

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.cameraDataStore: DataStore<Preferences> by preferencesDataStore(name = "camera_state")

@Singleton
class CameraStateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ds = context.cameraDataStore

    private val latKey = doublePreferencesKey("camera_lat")
    private val lonKey = doublePreferencesKey("camera_lon")
    private val zoomKey = doublePreferencesKey("camera_zoom")

    val cameraState: Flow<CameraState> = ds.data.map { p ->
        CameraState(
            latitude = p[latKey] ?: 55.7558,
            longitude = p[lonKey] ?: 37.6176,
            zoom = p[zoomKey] ?: 10.0
        )
    }

    suspend fun save(state: CameraState) {
        ds.edit { p ->
            p[latKey] = state.latitude
            p[lonKey] = state.longitude
            p[zoomKey] = state.zoom
        }
    }
}