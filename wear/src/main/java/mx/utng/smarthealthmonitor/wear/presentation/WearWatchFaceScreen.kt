package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WearWatchFaceScreen(onExit: () -> Unit) {
    var time by remember { mutableStateOf(LocalTime.now()) }
    val fc by SmartHealthRepository.fc.collectAsState()

    // Actualizar la hora cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            time = LocalTime.now()
            delay(1000L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Hora grande (como en el Renderer)
            Text(
                text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                fontSize = 50.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // Segundos pequeños
            Text(
                text = time.format(DateTimeFormatter.ofPattern("ss")),
                fontSize = 18.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(15.dp))

            // FC Real desde el Repositorio
            Text(
                text = "❤ $fc bpm",
                fontSize = 24.sp,
                color = Color.Red,
                fontWeight = FontWeight.Medium
            )
            
            // Botón pequeño para salir y volver a la app normal
            Spacer(modifier = Modifier.height(10.dp))
            androidx.wear.compose.material.Button(
                onClick = onExit,
                modifier = Modifier.size(30.dp),
                colors = androidx.wear.compose.material.ButtonDefaults.secondaryButtonColors()
            ) {
                Text("X", fontSize = 10.sp)
            }
        }
    }
}
