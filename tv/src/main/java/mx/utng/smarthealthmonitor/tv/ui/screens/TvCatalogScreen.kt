package mx.utng.smarthealthmonitor.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(onCardClick: (Int) -> Unit, viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (state.lecturas.isEmpty()) {
            Text("No hay lecturas disponibles", color = Color.White)
        } else {
            Button(onClick = { onCardClick(state.lecturas.first().id) }) {
                Text("Ver Detalle del Primero (${state.lecturas.first().bpm} bpm)")
            }
        }
    }
}
