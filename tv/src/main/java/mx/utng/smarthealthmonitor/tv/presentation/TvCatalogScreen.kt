package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
            .background(Color(0xFF0D1117)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Catálogo de Salud SmartHealth", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (state.lecturas.isEmpty()) {
                Button(onClick = { onCardClick(999) }) {
                    Text("Abrir Registro de Prueba (999)")
                }
            } else {
                Button(onClick = { onCardClick(state.lecturas.first().id) }) {
                    Text("Ver Detalle: ${state.lecturas.first().bpm} bpm")
                }
            }
        }
    }
}
