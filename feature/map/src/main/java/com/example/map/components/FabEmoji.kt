package com.example.map.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FabEmoji(symbol: String, modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick, modifier = modifier.size(56.dp)) {
        Text(symbol)
    }
}