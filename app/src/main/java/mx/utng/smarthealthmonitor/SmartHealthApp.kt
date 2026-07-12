package mx.utng.smarthealthmonitor

import android.app.Application
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 1. Inicialización de Room (Crítico para el Dashboard)
        try {
            SmartHealthRepository.init(this)
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Error inicializando repositorio: ${e.message}")
        }

        // 2. Inicialización SEGURA de Cast SDK en el hilo principal
        // El Cast SDK requiere el hilo principal, pero lo envolvemos en try-catch
        // para que si falla (por falta de Play Services), la app NO se cierre.
        try {
            CastContext.getSharedInstance(this)
            Log.d("SmartHealthApp", "Cast SDK inicializado con éxito")
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Cast SDK no disponible: ${e.message}")
        }
    }
}
