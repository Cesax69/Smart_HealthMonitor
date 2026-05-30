package mx.utng.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.utng.smarthealthmonitor.navigation.SmartHealthNavGraph
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHealthMonitorTheme {
                SmartHealthNavGraph()
            }
        }
    }
}
