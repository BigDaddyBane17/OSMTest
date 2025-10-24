package com.example.map.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
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
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
) {
    val density = LocalDensity.current
    val radius = 72.dp
    val fabSize = 56.dp
    val offsetY = 15.dp

    val rPx = with(density) { radius.toPx() }
    val halfFabPx = with(density) { fabSize.toPx() / 2f }
    val offsetYPx = with(density) { offsetY.toPx() }

    fun pos(angleDeg: Float): IntOffset {
        val rad = Math.toRadians(angleDeg.toDouble())
        val cx = center.x + rPx * cos(rad) - halfFabPx
        val cy = center.y + rPx * sin(rad) - halfFabPx + offsetYPx
        return IntOffset(cx.roundToInt(), cy.roundToInt())
    }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onDismiss() })
            }
    )

    FabEmoji("\uD83C\uDF24\uFE0F", Modifier.absoluteOffset { pos(-90f) }, onWeather)
    FabEmoji("ℹ\uFE0F",  Modifier.absoluteOffset { pos(0f) }, onInfo)
    FabEmoji("↔\uFE0F",  Modifier.absoluteOffset { pos(90f) }, onMove)
    FabEmoji("\uD83D\uDDD1\uFE0F️",  Modifier.absoluteOffset { pos(180f) }, onDelete)
}
