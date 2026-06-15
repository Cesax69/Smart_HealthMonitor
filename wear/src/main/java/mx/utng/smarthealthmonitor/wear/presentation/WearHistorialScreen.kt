package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.*
import androidx.wear.compose.material.*
import mx.utng.smarthealthmonitor.shared.data.LecturaFC

@Composable
fun WearHistorialScreen(
    onBack: () -> Unit,
    viewModel: WearDashboardViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsState()

    // Pantalla ultra-visible con fondo gris para notar el cambio
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("PANTALLA HISTORIAL", color = Color.Green)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Registros: ${historial.size}")
            
            historial.take(3).forEach { lectura ->
                Text("${lectura.valorBpm} bpm - ${lectura.hora}", style = MaterialTheme.typography.caption2)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(onClick = onBack) {
                Text("CERRAR")
            }
        }
    }
}
