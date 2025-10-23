package com.example.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun RadialMenu(
    center: Offset,
    onWeather: () -> Unit,
    onInfo: () -> Unit,
    onMove: () -> Unit,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current
    val radius = 72.dp
    val fabSize = 56.dp
    val rPx = with(density) { radius.toPx() }
    val halfFabPx = with(density) { fabSize.toPx() / 2f }

    fun pos(angleDeg: Float): IntOffset {
        val rad = Math.toRadians(angleDeg.toDouble())
        val cx = center.x + rPx * cos(rad) - halfFabPx
        val cy = center.y + rPx * sin(rad) - halfFabPx
        return IntOffset(cx.roundToInt(), cy.roundToInt())
    }


    Box(Modifier.fillMaxSize().clickable(onClick = onDismiss))

    FabEmoji("🌤️", Modifier.absoluteOffset { pos(-90f) }, onWeather)
    FabEmoji("ℹ️",  Modifier.absoluteOffset { pos(30f) }, onInfo)
    FabEmoji("↔️",  Modifier.absoluteOffset { pos(150f) }, onMove)
}

