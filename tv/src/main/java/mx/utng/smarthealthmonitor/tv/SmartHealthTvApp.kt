package mx.utng.smarthealthmonitor.tv

import android.app.Application
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository

class SmartHealthTvApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar Room en el módulo TV
        try {
            SmartHealthRepository.init(this)
        } catch (e: Exception) {
            android.util.Log.e("TV_APP", "Error init repo: ${e.message}")
        }
    }
}
