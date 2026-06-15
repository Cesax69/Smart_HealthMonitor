package mx.utng.smarthealthmonitor.wear.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.wear.compose.material.Text
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearTheme {
                var showHistory by remember { mutableStateOf(false) }

                if (!showHistory) {
                    WearDashboardScreen(
                        onAlertClick = { },
                        onHistoryClick = {
                            Toast.makeText(this@WearMainActivity, "Switching to History", Toast.LENGTH_SHORT).show()
                            showHistory = true
                        }
                    )
                } else {
                    WearHistorialScreen(
                        onBack = { showHistory = false }
                    )
                }
            }
        }
    }
}
