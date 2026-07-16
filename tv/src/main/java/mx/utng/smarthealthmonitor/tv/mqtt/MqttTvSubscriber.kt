package mx.utng.smarthealthmonitor.tv.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import mx.utng.smarthealthmonitor.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
 
class MqttTvSubscriber(
    private val context : Context,
    private val tvFlow  : MutableStateFlow<TvMessage?>
) {
    private var client: MqttAsyncClient? = null
 
    fun connect() {
        if (client?.isConnected == true) return

        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.generateId("tv"), 
                MemoryPersistence()
            )
     
            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        try {
                            val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                            tvFlow.value = tvMsg
                            Log.d("MQTT_TV","📺 Dato recibido de la Nube: ${tvMsg.bpm} bpm")
                        } catch (e: Exception) {
                            Log.e("MQTT_TV", "Error decodificando TV: ${e.message}")
                        }
                    }
                }
                override fun connectionLost(cause: Throwable?) {
                    Log.w("MQTT_TV", "TV perdió conexión, reintentando...")
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
     
            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
            }
     
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                    Log.d("MQTT_TV","✅ TV Conectada y escuchando al Teléfono")
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e("MQTT_TV","❌ TV Error conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("MQTT_TV", "❌ Error inicialización TV: ${e.message}")
        }
    }
    fun disconnect() { try { client?.disconnect() } catch(e: Exception) {} }
}
