package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {
            // Item 1: Card de FC
            item {
                WearFCCard(
                    fc = fc,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }

            // Item 2: CompactChip de Pasos
            item {
                CompactChip(
                    label = { 
                        Text(text = if (pasos == 0) "-- pasos" else "$pasos pasos") 
                    },
                    onClick = { /* Opcional */ },
                    colors = ChipDefaults.secondaryChipColors(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            // Item 3: Chip de Historial
            item {
                Chip(
                    label = { Text("🕒 Historial") },
                    onClick = {
                        android.util.Log.d("SmartHealthNav", "Click on Historial Chip")
                        onHistoryClick()
                    },
                    colors = ChipDefaults.secondaryChipColors(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            // Item 4: Chip de Alerta
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
