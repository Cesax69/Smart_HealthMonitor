package mx.utng.smarthealthmonitor.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
 
/**
 * SERVICIO PUENTE (BRIDGE) REACTIVO
 */
class MqttAppService(private val context: Context) {
    private var client: MqttAsyncClient? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
 
    fun connect() {
        if (client?.isConnected == true) return

        try {
            client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.generateId("app"), MemoryPersistence())
     
            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
            }
     
            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_FC) handleRemoteFc(msg)
                }
                override fun connectionLost(cause: Throwable?) {}
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
     
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_FC, MqttConfig.QOS)
                    Log.d("MQTT_APP", "✅ Puente App Conectado")
                    iniciarPuenteReactivo()
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e("MQTT_APP", "❌ Fallo puente: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("MQTT_APP", "Error: ${e.message}")
        }
    }

    private fun iniciarPuenteReactivo() {
        scope.launch {
            // Cada vez que el repositorio cambie (por Bluetooth, Reloj o Simulación local)
            // se re-publica a la TV.
            SmartHealthRepository.fcFlow.collect { bpm ->
                if (bpm > 0) rePublicarATV(bpm)
            }
        }
    }
 
    private fun handleRemoteFc(msg: MqttMessage) {
        try {
            val fcMsg = Json.decodeFromString<FcMessage>(String(msg.payload))
            Log.d("MQTT_APP", "📥 Reloj -> App: ${fcMsg.bpm}")
            scope.launch {
                SmartHealthRepository.actualizarFC(fcMsg.bpm)
            }
        } catch (e: Exception) {
            Log.e("MQTT_APP", "Err decod: ${e.message}")
        }
    }

    private fun rePublicarATV(bpm: Int) {
        if (client?.isConnected != true) return
        try {
            val tvMsg = TvMessage(
                bpm = bpm,
                estado = if (bpm in 60..100) "Normal" else "Crítico",
                hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            )
            val payload = Json.encodeToString(tvMsg).toByteArray()
            client?.publish(MqttConfig.TOPIC_TV, MqttMessage(payload).apply { 
                qos = MqttConfig.QOS; isRetained = true 
            })
            Log.d("MQTT_APP", "🔁 App -> TV: $bpm bpm")
        } catch (e: Exception) {
            Log.e("MQTT_APP", "Err re-pub: ${e.message}")
        }
    }
 
    fun disconnect() { 
        scope.cancel()
        try { client?.disconnect() } catch(e: Exception) {} 
    }
}
