package mx.utng.smarthealthmonitor.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearTheme {
                // Estado para alternar entre el diseño de la carátula y la app normal
                var isWatchFaceMode by remember { mutableStateOf(true) }

                if (isWatchFaceMode) {
                    WearWatchFaceScreen(onExit = { isWatchFaceMode = false })
                } else {
                    SmartHealthWearNavGraph()
                }
            }
        }
    }
}
