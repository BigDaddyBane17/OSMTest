package com.example.osmtest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration
import java.io.File

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = Configuration.getInstance()
        config.userAgentValue = packageName
        val base = File(cacheDir, "osmdroid").apply { mkdirs() }
        config.osmdroidBasePath = base
        config.osmdroidTileCache = File(base, "tiles").apply { mkdirs() }
    }
}