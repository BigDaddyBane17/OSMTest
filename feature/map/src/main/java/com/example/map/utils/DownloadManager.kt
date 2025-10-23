package com.example.map.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri

fun enqueueDownload(
    context: Context,
    url: String,
    filename: String,
    mime: String = "text/csv"
) {
    val request = DownloadManager.Request(url.toUri())
        .setTitle(filename)
        .setDescription("Скачивание прогноза погоды")
        .setMimeType(mime)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

    val dm = context.getSystemService(DownloadManager::class.java)
    dm.enqueue(request)
}