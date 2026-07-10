package mx.utng.smarthealthmonitor.tv

import android.app.Application
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository

class SmartHealthTvApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar Room y el repositorio compartido para la TV
        SmartHealthRepository.init(this)
    }
}
