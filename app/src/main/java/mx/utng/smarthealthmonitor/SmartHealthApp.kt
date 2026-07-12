package mx.utng.smarthealthmonitor

import android.app.Application
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository
import java.util.concurrent.Executors

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 1. Inicialización de Room (Crítico)
        try {
            SmartHealthRepository.init(this)
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Error inicializando repositorio: ${e.message}")
        }

        // 2. Inicialización de Cast SDK en hilo separado para evitar bloquear UI
        // y prevenir cierres si los Google Play Services fallan
        Executors.newSingleThreadExecutor().execute {
            try {
                CastContext.getSharedInstance(this)
                Log.d("SmartHealthApp", "Cast SDK inicializado correctamente")
            } catch (e: Exception) {
                Log.e("SmartHealthApp", "Cast SDK no disponible en este dispositivo: ${e.message}")
            }
        }
    }
}
