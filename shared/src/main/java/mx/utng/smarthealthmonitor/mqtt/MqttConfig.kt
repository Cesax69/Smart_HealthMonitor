package mx.utng.smarthealthmonitor.mqtt

object MqttConfig {
    const val BROKER_URL  = "ssl://160c54ad3c594afca12c0c5db42ad6ca.s1.eu.hivemq.cloud:8883"
    const val USERNAME    = "cesarmqtt"
    const val PASSWORD    = "CesarMqtt2024*"
 
    // Topics UTNG
    const val TOPIC_FC    = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV    = "utng/smarthealthmonitor/tv"
 
    const val QOS = 1
 
    fun generateId(prefix: String) = "$prefix-${(1000..9999).random()}"
}
