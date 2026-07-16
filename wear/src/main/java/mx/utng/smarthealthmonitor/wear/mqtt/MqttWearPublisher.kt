package mx.utng.smarthealthmonitor.wear.mqtt

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.utng.smarthealthmonitor.mqtt.MqttConfig
import mx.utng.smarthealthmonitor.mqtt.FcMessage
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
 
class MqttWearPublisher(private val context: Context) {
 
    private var client: MqttAsyncClient? = null
 
    fun connect() {
        if (client?.isConnected == true) return

        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.generateId("wear"),
                MemoryPersistence()
            )
 
            val options = MqttConnectOptions().apply {
                userName        = MqttConfig.USERNAME
                password        = MqttConfig.PASSWORD.toCharArray()
                isCleanSession  = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
            }
 
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT_WEAR", "✅ Conectado a HiveMQ Cloud")
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e("MQTT_WEAR", "❌ Error conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("MQTT_WEAR", "❌ Error al crear cliente: ${e.message}")
        }
    }
 
    fun publishFC(bpm: Int, estado: String) {
        if (client?.isConnected != true) {
            Log.w("MQTT_WEAR", "⚠️ No se pudo publicar: Cliente desconectado")
            connect() // Intentar reconectar
            return
        }
 
        try {
            val message = FcMessage(bpm = bpm, estado = estado)
            val payload = Json.encodeToString(message).toByteArray()
     
            val mqttMessage = MqttMessage(payload).apply {
                qos      = MqttConfig.QOS
                isRetained = true
            }
     
            client?.publish(MqttConfig.TOPIC_FC, mqttMessage)
            Log.d("MQTT_WEAR", "📤 Publicado: $bpm bpm")
        } catch (e: Exception) {
            Log.e("MQTT_WEAR", "❌ Error al publicar: ${e.message}")
        }
    }
 
    fun disconnect() { try { client?.disconnect() } catch(e: Exception) {} }
}
