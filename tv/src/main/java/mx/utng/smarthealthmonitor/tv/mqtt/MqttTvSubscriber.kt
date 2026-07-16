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
    private val tvFlow  : MutableStateFlow<TvMessage?>,
    private val onStatus: (String) -> Unit
) {
    private var client: MqttAsyncClient? = null
 
    fun connect() {
        if (client?.isConnected == true) return
        onStatus("Conectando...")

        try {
            client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.generateId("tv"), MemoryPersistence())
     
            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
            }

            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                        tvFlow.value = tvMsg
                        Log.d("MQTT_TV","📺 TV Recibió: ${tvMsg.bpm}")
                    }
                }
                override fun connectionLost(cause: Throwable?) {
                    onStatus("Desconectado")
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
     
            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                    onStatus("En línea")
                    Log.d("MQTT_TV","✅ TV Conectada")
                }
                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    onStatus("Error: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            onStatus("Error Init")
        }
    }
    fun disconnect() { try { client?.disconnect() } catch(e: Exception) {} }
}
