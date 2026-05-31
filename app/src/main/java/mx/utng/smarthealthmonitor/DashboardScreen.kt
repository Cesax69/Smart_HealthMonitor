package mx.utng.smarthealthmonitor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val spo2 by viewModel.spo2.collectAsState()
    val historial = viewModel.historial

    SmartHealthMonitorTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAlertClick,
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Alerta")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Dashboard de Monitoreo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // Fila de Indicadores Principales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TarjetaDato(
                        titulo = "Ritmo",
                        valor = "$fc",
                        unidad = "bpm",
                        icono = Icons.Default.Favorite,
                        colorIcono = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaDato(
                        titulo = "Oxígeno",
                        valor = "$spo2",
                        unidad = "%",
                        icono = Icons.Default.Opacity,
                        colorIcono = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TarjetaDato(
                    titulo = "Actividad Física",
                    valor = "%,d".format(pasos),
                    unidad = "pasos hoy",
                    icono = Icons.Default.DirectionsWalk,
                    colorIcono = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historial Reciente",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(onClick = onHistorialClick) {
                        Text("Ver todo")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        historial.take(3).forEach { registro ->
                            Text(
                                text = "• ${registro.bpm} bpm - ${registro.fecha}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de simulación — SOLO PARA DEBUG
                OutlinedButton(
                    onClick = {
                        // Simular lectura del wearable
                        val fcSimulado = (60..110).random()
                        val pasosSimulados = (3000..8000).random()
                        val spo2Simulado = (95..100).random()
                        
                        SmartHealthRepository.actualizarFC(fcSimulado)
                        SmartHealthRepository.actualizarPasos(pasosSimulados)
                        SmartHealthRepository.actualizarSpO2(spo2Simulado)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simular dato del wearable (DEBUG)")
                }
            }
        }
    }
}

@Composable
fun TarjetaDato(
    titulo: String,
    valor: String,
    unidad: String,
    icono: ImageVector,
    colorIcono: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = titulo, style = MaterialTheme.typography.labelMedium)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = valor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorIcono
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unidad,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}
