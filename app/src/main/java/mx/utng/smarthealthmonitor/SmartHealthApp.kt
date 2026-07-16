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
        
        // 1. Inicialización de Room (Crítico)
        try {
            SmartHealthRepository.init(this)
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Error Room: ${e.message}")
        }

        // 2. Inicialización de MQTT Puente (Seguro)
        try {
            mqttService = MqttAppService(this)
            mqttService.connect()
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "Error MQTT: ${e.message}")
        }

        // 3. Inicialización SEGURA de Cast SDK
        // Usamos un hilo separado porque CastContext.getSharedInstance puede tardar mucho
        // en hardware real y disparar el cierre de Android por falta de respuesta.
        Thread {
            try {
                CastContext.getSharedInstance(this)
                Log.d("SmartHealthApp", "Cast SDK ok")
            } catch (e: Exception) {
                Log.e("SmartHealthApp", "Cast SDK fail: ${e.message}")
            }
        }.start()
    }
}
