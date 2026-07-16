package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(onCardClick: (Int) -> Unit, viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))) {
    val state by viewModel.state.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("SmartHealth Monitor (MQTT)", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Text("Estado: ${state.connectionStatus}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                onClick = {},
                modifier = Modifier.padding(16.dp),
                shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.medium)
            ) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ritmo Cardíaco Actual", style = MaterialTheme.typography.titleMedium)
                    
                    if (state.fcActual == 0) {
                        Text("Esperando latidos...", style = MaterialTheme.typography.labelSmall)
                    } else {
                        Text("${state.fcActual} bpm", style = MaterialTheme.typography.displayLarge, color = Color.Red)
                        Text("Estado: ${state.fcEstado}", style = MaterialTheme.typography.bodyLarge)
                        Text("Última actualización: ${state.ultimaHora}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            if (state.lecturas.isEmpty()) {
                Button(onClick = { onCardClick(999) }) {
                    Text("Simular Historial")
                }
            } else {
                Button(onClick = { onCardClick(state.lecturas.first().id) }) {
                    Text("Ver Detalle: ${state.lecturas.first().bpm} bpm")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Reto Extra: Consultas avanzadas de PostgreSQL Neon
            Text("Estadísticas del Día (Nube Neon)", style = MaterialTheme.typography.titleMedium, color = Color.LightGray)
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Promedio Bpm", style = MaterialTheme.typography.labelSmall)
                    Text("${state.promedioBpm}", style = MaterialTheme.typography.titleLarge, color = Color.Cyan)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Picos de Estrés", style = MaterialTheme.typography.labelSmall)
                    Text("${state.picosEstres}", style = MaterialTheme.typography.titleLarge, color = Color.Red)
                }
            }
        }
    }
}
