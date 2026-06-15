package mx.utng.smarthealthmonitor.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearTheme {
                var currentScreen by remember { mutableStateOf("dashboard") }

                when (currentScreen) {
                    "dashboard" -> {
                        WearDashboardScreen(
                            onAlertClick = { /* No necesario para esta prueba */ },
                            onHistoryClick = { currentScreen = "historial" }
                        )
                    }
                    "historial" -> {
                        WearHistorialScreen(
                            onBack = { currentScreen = "dashboard" }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
fun WearDashboardPreview() {
    SmartHealthWearTheme {
        WearDashboardScreen(onAlertClick = {}, onHistoryClick = {})
    }
}
