package mx.utng.smarthealthmonitor

import android.app.Application
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            SmartHealthRepository.init(this) // inicializar Room
            // Inicialización segura del Cast SDK para evitar cierres en dispositivos sin Play Services
            CastContext.getSharedInstance(this)
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Error durante la inicialización: ${e.message}")
        }
    }
}
