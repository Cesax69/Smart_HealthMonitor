package mx.utng.smarthealthmonitor

import android.app.Application
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import mx.utng.smarthealthmonitor.mqtt.MqttAppService
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository as BaseSharedRepo
import java.util.concurrent.Executors

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        
        // 1. Inicialización de Room
        try {
            SmartHealthRepository.init(this)
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Error inicializando repositorio: ${e.message}")
        }

        // 2. Inicialización de MQTT
        mqttService = MqttAppService(
            context = this,
            fcFlow  = BaseSharedRepo.fc
        )
        mqttService.connect()

        // 3. Inicialización SEGURA de Cast SDK
        try {
            CastContext.getSharedInstance(this)
            Log.d("SmartHealthApp", "Cast SDK inicializado con éxito")
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Cast SDK no disponible: ${e.message}")
        }
    }
}
