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
 
    // Generación dinámica de IDs para evitar que los dispositivos se expulsen entre sí
    fun generateId(prefix: String) = "$prefix-${(1000..9999).random()}"

    // IDs estáticos (Mantenidos por compatibilidad, pero se recomienda usar generateId)
    const val CLIENT_WEAR = "shm-wear-device"
    const val CLIENT_APP  = "shm-phone-device"
    const val CLIENT_TV   = "shm-tv-device"
}
