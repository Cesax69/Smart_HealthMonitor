package mx.utng.smarthealthmonitor.tv.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import mx.utng.smarthealthmonitor.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory
 
class MqttTvSubscriber(
    private val context : Context,
    private val tvFlow  : MutableStateFlow<TvMessage?>,
    private val onStatus: (String) -> Unit
) {
    private var client: MqttAsyncClient? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
 
    fun connect() {
        if (client?.isConnected == true) return
        
        onStatus("Intentando conectar...")

        try {
            val clientId = MqttConfig.generateId("tv-emu")
            
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                clientId,
                MemoryPersistence()
            )
     
            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = SSLSocketFactory.getDefault()
            }

            // Usamos MqttCallbackExtended para tener el evento de conexión completada
            client?.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                    try {
                        client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                        onStatus("En línea")
                        Log.d("MQTT_TV", "✅ Conexión establecida")
                    } catch (e: Exception) {
                        onStatus("Error Suscripción")
                    }
                }
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        try {
                            val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                            tvFlow.value = tvMsg
                            Log.d("MQTT_TV", "📥 Recibido: ${tvMsg.bpm}")
                        } catch (e: Exception) {
                            Log.e("MQTT_TV", "❌ Error decod: ${e.message}")
                        }
                    }
                }
                override fun connectionLost(cause: Throwable?) {
                    onStatus("Reconectando...")
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
     
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT_TV", "🚀 Handshake exitoso")
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    val error = exception?.message ?: "Desconocido"
                    onStatus("Fallo: $error")
                    Log.e("MQTT_TV", "❌ Fallo conexión: $error")
                    
                    scope.launch {
                        delay(5000)
                        connect()
                    }
                }
            })
        } catch (e: Exception) {
            onStatus("Error Init")
        }
    }

    fun disconnect() {
        scope.cancel()
        try { client?.disconnect() } catch (e: Exception) {}
    }
}
