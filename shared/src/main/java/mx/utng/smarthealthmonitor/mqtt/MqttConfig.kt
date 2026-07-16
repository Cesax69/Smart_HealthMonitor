package mx.utng.smarthealthmonitor.mqtt

object MqttConfig {
    const val BROKER_URL  = "ssl://160c54ad3c594afca12c0c5db42ad6ca.s1.eu.hivemq.cloud:8883"
    const val USERNAME    = "SmartHealth"
    const val PASSWORD    = "SmartHealth123"
 
    // Topics
    const val TOPIC_FC    = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV    = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"
 
    const val QOS = 1
 
    // Mantenemos compatibilidad con IDs estáticos pero permitimos generación dinámica
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP  = "smarthealthmonitor-app"
    const val CLIENT_TV   = "smarthealthmonitor-tv"

    fun generateId(prefix: String) = "$prefix-${System.currentTimeMillis() % 10000}"
}
