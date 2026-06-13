package mx.utng.smarthealthmonitor.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearTheme {
                SmartHealthWearNavGraph()
            }
        }
    }
}

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
fun WearDashboardPreview() {
    SmartHealthWearTheme {
        WearDashboardScreen()
    }
}

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
fun WearAlertaPreview() {
    SmartHealthWearTheme {
        WearAlertaScreen(fc = 120, onConfirmar = {}, onCancelar = {})
    }
}
