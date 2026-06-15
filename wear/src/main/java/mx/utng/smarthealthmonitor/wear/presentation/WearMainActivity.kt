package mx.utng.smarthealthmonitor.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.widget.Toast.makeText(this, "WatchFace Instalada v3", android.widget.Toast.LENGTH_LONG).show()
        setContent {
            SmartHealthWearTheme {
                SmartHealthWearNavGraph()
            }
        }
    }
}
