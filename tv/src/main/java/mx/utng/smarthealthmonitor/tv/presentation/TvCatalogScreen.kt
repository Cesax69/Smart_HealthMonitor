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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.tv.material3.*
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory
import mx.utng.smarthealthmonitor.shared.data.remote.LecturaFcDto

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(onCardClick: (Int) -> Unit, viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))) {
    val state by viewModel.state.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(24.dp)
    ) {
        if (state.isLoading) {
            Text("Cargando datos desde Neon...", color = Color.White, modifier = Modifier.align(Alignment.Center))
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("SmartHealth Monitor (Neon Serverless)", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                        Text("Estado MQTT: ${state.connectionStatus}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Button(onClick = { viewModel.refresh() }) {
                        Text("↺ Actualizar")
                    }
                }
            }

            // Fila 1: Estado Actual (3 dispositivos)
            item {
                Text("Estado Actual (3 dispositivos)", style = MaterialTheme.typography.titleLarge, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    val recientes = state.lecturasRecientesNeon
                    if (recientes.isEmpty()) {
                        item { Text("Sin datos recientes", color = Color.Gray) }
                    } else {
                        items(recientes) { lectura ->
                            CardInfoDispositivo(lectura, onCardClick)
                        }
                    }
                }
            }

            // Fila 2: Historial Completo
            item {
                Text("Historial Completo (Últimas 50 lecturas)", style = MaterialTheme.typography.titleLarge, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (state.historialNeon.isEmpty()) {
                        item { Text("Sin historial", color = Color.Gray) }
                    } else {
                        items(state.historialNeon) { lectura ->
                            CardInfoDispositivo(lectura, onCardClick)
                        }
                    }
                }
            }

            // Fila 3: Consultas avanzadas
            item {
                Text("Análisis y Consultas Avanzadas", style = MaterialTheme.typography.titleLarge, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        Surface(
                            onClick = {},
                            modifier = Modifier.size(220.dp, 120.dp),
                            shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.medium)
                        ) {
                            Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                Text("Taquicardias Sostenidas", style = MaterialTheme.typography.labelMedium)
                                Text("${state.taquicardiaSostenida} episodios", style = MaterialTheme.typography.titleLarge, color = Color.Red)
                            }
                        }
                    }
                    items(state.alertasNeon) { alerta ->
                        Surface(
                            onClick = {},
                            modifier = Modifier.size(220.dp, 120.dp),
                            shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.medium)
                        ) {
                            Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                Text("Alerta 24h: ${alerta.dispositivo}", style = MaterialTheme.typography.labelMedium)
                                Text("${alerta.bpm} bpm", style = MaterialTheme.typography.titleLarge, color = Color.Yellow)
                                Text(alerta.estado, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    items(state.promediosHoraNeon) { promedio ->
                        Surface(
                            onClick = {},
                            modifier = Modifier.size(220.dp, 120.dp),
                            shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.medium)
                        ) {
                            Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                Text("Hora ${promedio.hora}:00", style = MaterialTheme.typography.labelMedium)
                                Text("Promedio: ${promedio.bpm} bpm", style = MaterialTheme.typography.titleLarge, color = Color.Cyan)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CardInfoDispositivo(lectura: LecturaFcDto, onCardClick: (Int) -> Unit) {
    Surface(
        onClick = { onCardClick(lectura.id) },
        modifier = Modifier.size(200.dp, 120.dp),
        shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.medium)
    ) {
        Column(
            Modifier.padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(lectura.dispositivo.uppercase(), style = MaterialTheme.typography.titleMedium, color = Color.Cyan)
            Text("${lectura.bpm} bpm", style = MaterialTheme.typography.displaySmall, color = if (lectura.bpm > 100) Color.Red else Color.White)
            Text(lectura.estado, style = MaterialTheme.typography.bodySmall)
            Text(lectura.hora, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}
