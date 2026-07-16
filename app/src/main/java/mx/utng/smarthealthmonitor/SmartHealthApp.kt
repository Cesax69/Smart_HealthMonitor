package mx.utng.smarthealthmonitor

import android.app.Application
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import mx.utng.smarthealthmonitor.mqtt.MqttAppService
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository

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

        // 2. Inicialización de MQTT Puente
        mqttService = MqttAppService(
            context = this,
            fcFlow  = SmartHealthRepository.fcFlow as kotlinx.coroutines.flow.MutableStateFlow<Int>
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
