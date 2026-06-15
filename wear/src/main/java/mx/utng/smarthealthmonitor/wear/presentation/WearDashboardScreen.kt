package mx.utng.smarthealthmonitor.wear.presentation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import mx.utng.smarthealthmonitor.wear.presentation.components.WearFCCard

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: WearDashboardViewModel = viewModel()
) {
    val context = LocalContext.current
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {
            // BOTÓN TEMPORAL PARA FORZAR CARÁTULA
            item {
                Chip(
                    label = { Text("⚙ ACTIVAR CARÁTULA") },
                    onClick = {
                        val intent = Intent(Intent.ACTION_SET_WALLPAPER)
                        context.startActivity(Intent.createChooser(intent, "Selecciona SmartHealth"))
                    },
                    colors = ChipDefaults.primaryChipColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }
            // 1. Card de FC
            item {
                WearFCCard(
                    fc = fc,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }

            // 2. Chip de Pasos
            item {
                CompactChip(
                    label = { 
                        Text(text = if (pasos == 0) "-- pasos" else "$pasos pasos") 
                    },
                    onClick = { },
                    colors = ChipDefaults.secondaryChipColors(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )
            }

            // 3. Botón de Historial (Restaurado a su posición original)
            item {
                Chip(
                    label = { Text("🕒 Historial") },
                    onClick = onHistoryClick,
                    colors = ChipDefaults.secondaryChipColors(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            // 4. Botón de Alerta
            item {
                Chip(
                    label = { Text("⚠ Alerta") },
                    onClick = onAlertClick,
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }
        }
    }
}
