package com.example.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CenterCrosshair() {
    Box(Modifier.fillMaxSize()) {
        val size = 14.dp
        val stroke = 1.dp
        Box(
            Modifier
                .width(stroke)
                .height(size)
                .align(Alignment.Center)
                .background(Color.Red),
        )
        Box(
            Modifier
                .height(stroke)
                .width(size)
                .align(Alignment.Center)
                .background(Color.Red)
        )
    }
}