package com.example.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CameraPreferencesDataSource @Inject constructor(
    private val ds: DataStore<Preferences>
) {

    private val latKey = doublePreferencesKey("camera_lat")
    private val lonKey = doublePreferencesKey("camera_lon")
    private val zoomKey = doublePreferencesKey("camera_zoom")


    fun observe() = ds.data.map { p ->
        Triple(
            p[latKey] ?: 55.751244,
            p[lonKey] ?: 37.618423,
            p[zoomKey] ?: 15.0
        )
    }

    suspend fun save(lat: Double, lon: Double, zoom: Double) {
        ds.edit { p ->
            p[latKey] = lat
            p[lonKey] = lon
            p[zoomKey] = zoom
        }
    }


}