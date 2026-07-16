package mx.utng.smarthealthmonitor.wear.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {

    // SOLICITUD DE PERMISOS PARA QUE EL SENSOR FUNCIONE
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Permisos concedidos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Pedir permisos en el arranque
        pedirPermisos()

        setContent {
            SmartHealthWearTheme {
                var isWatchFaceMode by remember { mutableStateOf(true) }

                if (isWatchFaceMode) {
                    WearWatchFaceScreen(onExit = { isWatchFaceMode = false })
                } else {
                    SmartHealthWearNavGraph()
                }
            }
        }
    }

    private fun pedirPermisos() {
        val permissions = arrayOf(
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
        
        val needed = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        
        if (needed.isNotEmpty()) {
            requestPermissionLauncher.launch(needed.toTypedArray())
        }
    }
}
