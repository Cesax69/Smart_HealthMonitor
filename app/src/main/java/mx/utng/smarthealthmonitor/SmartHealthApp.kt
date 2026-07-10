package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this) // inicializar Room en el repositorio compartido
    }
}
