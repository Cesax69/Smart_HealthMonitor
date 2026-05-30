package mx.utng.smarthealthmonitor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit,
    onAlertClick: () -> Unit
) {
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
            Text("Dashboard de Monitoreo", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen Semanal", style = MaterialTheme.typography.titleMedium)
                    Text("Pasos totales: 28,450", style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = onHistorialClick) {
                Text("Ver todo el historial")
            }
        }
    }
}
