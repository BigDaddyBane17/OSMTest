package com.example.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun ErrorBox(message: String, modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize().then(modifier), contentAlignment = Alignment.Center) {
        Text("Ошибка: $message", color = MaterialTheme.colorScheme.error)
    }
}