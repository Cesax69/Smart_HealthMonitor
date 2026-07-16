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
 * Observa el repositorio y publica a la TV automáticamente.
 */
class MqttAppService(private val context: Context) {
    private var client: MqttAsyncClient? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
 
    fun connect() {
        if (client?.isConnected == true) return

        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.generateId("phone"), 
                MemoryPersistence()
            )
     
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
                override fun connectionLost(cause: Throwable?) {
                    Log.w("MQTT_APP", "Conexión perdida, reintentando...")
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
     
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_FC, MqttConfig.QOS)
                    Log.d("MQTT_APP", "✅ Puente conectado y escuchando al Reloj")
                    iniciarPuenteReactivo()
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e("MQTT_APP", "❌ Error conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("MQTT_APP", "❌ Error: ${e.message}")
        }
    }

    private fun iniciarPuenteReactivo() {
        scope.launch {
            SmartHealthRepository.fcFlow.collect { bpm ->
                if (bpm > 0) rePublicarATV(bpm)
            }
        }
    }
 
    private fun handleRemoteFc(msg: MqttMessage) {
        try {
            val fcMsg = Json.decodeFromString<FcMessage>(String(msg.payload))
            Log.d("MQTT_APP", "📥 Dato recibido del Reloj: ${fcMsg.bpm}")
            
            // Actualizar repositorio centralizado
            scope.launch {
                SmartHealthRepository.actualizarFC(fcMsg.bpm)
            }
        } catch (e: Exception) {
            Log.e("MQTT_APP", "Error decodificando: ${e.message}")
        }
    }

    private fun rePublicarATV(bpm: Int) {
        if (client?.isConnected != true) return

        try {
            val estado = when { bpm < 60 -> "Baja"; bpm > 100 -> "Alta"; else -> "Normal" }
            val msg = TvMessage(
                bpm = bpm,
                estado = estado,
                hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            )
            val payload = Json.encodeToString(msg).toByteArray()
            client?.publish(MqttConfig.TOPIC_TV, MqttMessage(payload).apply { 
                qos = MqttConfig.QOS; isRetained = true 
            })
            Log.d("MQTT_APP", "🔁 Re-publicado a TV: $bpm bpm")
        } catch (e: Exception) {
            Log.e("MQTT_APP", "❌ Error puente: ${e.message}")
        }
    }
 
    fun disconnect() { 
        scope.cancel()
        try { client?.disconnect() } catch(e: Exception) {} 
    }
}
