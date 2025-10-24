package com.example.map.components

import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RadialMenu(
    center: Offset,
    onWeather: () -> Unit,
    onInfo: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val touchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()

    var startX by remember { mutableFloatStateOf(0f) }
    var startY by remember { mutableFloatStateOf(0f) }
    var moved  by remember { mutableStateOf(false) }

    val radius = 72.dp
    val fabSize = 56.dp
    val density = androidx.compose.ui.platform.LocalDensity.current
    val rPx = with(density) { radius.toPx() }
    val halfFab = with(density) { fabSize.toPx() / 2f }

    fun pos(angleDeg: Float): IntOffset {
        val rad = Math.toRadians(angleDeg.toDouble())
        val cx = center.x + rPx * cos(rad) - halfFab
        val cy = center.y + rPx * sin(rad) - halfFab
        return IntOffset(cx.roundToInt(), cy.roundToInt())
    }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInteropFilter { ev ->
                when (ev.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = ev.x; startY = ev.y; moved = false
                        false
                    }
                    MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                        moved = true
                        false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (!moved) {
                            val dx = ev.x - startX
                            val dy = ev.y - startY
                            if (dx*dx + dy*dy > touchSlop * touchSlop) moved = true
                        }
                        false
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!moved) onDismiss()
                        false
                    }
                    else -> false
                }
            }
    )

    FabEmoji("🌤️", Modifier.absoluteOffset { pos(-90f) }, onWeather)
    FabEmoji("ℹ️",  Modifier.absoluteOffset { pos(0f) },    onInfo)
    FabEmoji("↔️",  Modifier.absoluteOffset { pos(90f) },   onMove)
    FabEmoji("🗑️",  Modifier.absoluteOffset { pos(180f) },  onDelete)
}