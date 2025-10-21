package com.example.mapdetails

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.components.ErrorBox
import com.example.components.LoadingBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDetailsScreen(
    viewmodel: MapDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Информация о точке") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            MapDetailsUiState.Loading -> LoadingBox(Modifier.padding(padding))
            is MapDetailsUiState.Error -> ErrorBox(s.message, Modifier.padding(padding))
            is MapDetailsUiState.Content -> Content(s, Modifier.padding(padding))
        }
    }
}

@Composable
fun Content(s: MapDetailsUiState.Content, modifier: Modifier = Modifier) {
    Column(
        Modifier
            .fillMaxSize()
            .then(modifier)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Название", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(s.name.ifBlank { "Без имени" }, maxLines = 1, overflow = TextOverflow.Ellipsis)

                Spacer(Modifier.height(8.dp))

                Text("Координаты", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("lat: ${s.lat.format6()}, lon: ${s.lon.format6()}")
            }
        }

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Погода (актуальная)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("Температура: ${s.temperature?.let { "${it}°C" } ?: "—"}")
                Text("Ветер: ${s.windSpeed?.let { "$it м/с" } ?: "—"}")
            }
        }
    }
}





private fun Double.format6(): String = String.format("%.6f", this)
