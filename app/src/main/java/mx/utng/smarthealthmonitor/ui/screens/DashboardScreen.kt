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
            // FORZAMOS LA VISIBILIDAD CON UN WRAPPER DE TEMA Y TAMAÑO FIJO
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { context ->
                        // MediaRouteButton necesita un contexto que incluya el tema de AppCompat
                        // para renderizar el icono correctamente en dispositivos físicos
                        MediaRouteButton(context).apply {
                            CastButtonFactory.setUpMediaRouteButton(context, this)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { button ->
                        // Forzamos que sea visible y clickable
                        button.visibility = android.view.View.VISIBLE
                        button.isClickable = true
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
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
    
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = { nota ->
                mostrarAlerta = false
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = if (nota.isBlank()) "✅ Alerta enviada" else "✅ Alerta: $nota",
                        duration = SnackbarDuration.Long
                    )
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
                        contentDescription = "Alerta",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TarjetaDato("Ritmo", "$fc", "bpm", Icons.Default.Favorite, MaterialTheme.colorScheme.error, Modifier.weight(1f))
                    TarjetaDato("Oxígeno", "$spo2", "%", Icons.Default.Opacity, MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))

                TarjetaDato("Actividad Física", "%,d".format(pasos), "pasos hoy", Icons.Default.DirectionsWalk, MaterialTheme.colorScheme.primary, Modifier.fillMaxWidth())

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text("Historial Reciente", style = MaterialTheme.typography.titleLarge)
                    TextButton(onClick = onHistorialClick) { Text("Ver todo") }
                }

                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
                    Column(Modifier.padding(16.dp)) {
                        historial.take(3).forEach { registro ->
                            Text("• ${registro.valorBpm} bpm - ${registro.hora}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val fcSim = (60..110).random()
                            val pasSim = (3000..8000).random()
                            val spoSim = (95..100).random()
                            SmartHealthRepository.actualizarFC(fcSim)
                            SmartHealthRepository.actualizarPasos(pasSim)
                            SmartHealthRepository.actualizarSpO2(spoSim)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simular dato del wearable")
                }
            }
        }
    }
}

@Composable
fun TarjetaDato(titulo: String, valor: String, unidad: String, icono: ImageVector, colorIcono: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Icon(imageVector = icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(titulo, style = MaterialTheme.typography.labelMedium)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorIcono)
                Spacer(Modifier.width(4.dp))
                Text(unidad, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    SmartHealthMonitorTheme { DashboardScreen() }
}
