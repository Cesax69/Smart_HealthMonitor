package mx.utng.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            // CastButton: AndroidView que envuelve MediaRouteButton de forma segura
            AndroidView(
                factory = { context ->
                    try {
                        MediaRouteButton(context).apply {
                            CastButtonFactory.setUpMediaRouteButton(context, this)
                        }
                    } catch (e: Exception) {
                        // En caso de error (dispositivo sin Play Services), devolvemos un Spacer
                        android.widget.Space(context)
                    }
                },
                modifier = Modifier.size(48.dp)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {}, 
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsStateWithLifecycle()
    val pasos by viewModel.pasos.collectAsStateWithLifecycle()
    val spo2 by viewModel.spo2.collectAsStateWithLifecycle()
    val historial by viewModel.historial.collectAsStateWithLifecycle()
    
    // ── Estado del diálogo y Snackbar ──────────────────────
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ── Diálogo condicional ────────────────────────────────
    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = { nota ->
                mostrarAlerta = false
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = if (nota.isBlank()) "✅ Alerta enviada" else "✅ Alerta: $nota",
                        actionLabel = "Deshacer",
                        duration = SnackbarDuration.Long
                    )
                    
                    if (result == SnackbarResult.ActionPerformed) {
                        snackbarHostState.showSnackbar("Alerta cancelada")
                    }
                }
            }
        )
    }

    SmartHealthMonitorTheme {
        Scaffold(
            topBar = { DashboardTopBar("SmartHealth Dashboard") },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarAlerta = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
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
                                text = "• ${registro.valorBpm} bpm - ${registro.hora}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de simulación
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val fcSimulado = (60..110).random()
                            val pasosSimulados = (3000..8000).random()
                            val spo2Simulado = (95..100).random()
                            
                            SmartHealthRepository.actualizarFC(fcSimulado)
                            SmartHealthRepository.actualizarPasos(pasosSimulados)
                            SmartHealthRepository.actualizarSpO2(spo2Simulado)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Simular dato del wearable (PRUEBA)")
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
