package com.example.map.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun enqueueDownload(context: Context, url: String, filename: String, mime: String = "text/plain") = withContext(Dispatchers.IO) {
    val resolver = context.contentResolver

    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, filename)
        put(MediaStore.Downloads.MIME_TYPE, mime)
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        ?: throw IOException("Не удалось создать запись в MediaStore")

    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        connection.inputStream.use { input ->
            resolver.openOutputStream(uri)?.use { output ->
                input.copyTo(output)
            } ?: throw IOException("Не удалось открыть OutputStream для $uri")
        }
    } finally {
        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    withContext(Dispatchers.Main) {
        Toast.makeText(context, "Файл сохранён: $filename", Toast.LENGTH_SHORT).show()
    }

}

