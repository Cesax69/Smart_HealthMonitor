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
 
    // IDs ÚNICOS para evitar que un dispositivo expulse al otro del broker
    val CLIENT_WEAR = "shm-wear-${(100..999).random()}"
    val CLIENT_APP  = "shm-phone-${(100..999).random()}"
    val CLIENT_TV   = "shm-tv-${(100..999).random()}"
}
