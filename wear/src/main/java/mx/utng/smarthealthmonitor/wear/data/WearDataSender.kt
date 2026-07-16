package mx.utng.smarthealthmonitor.wear.data

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.wear.mqtt.MqttWearPublisher

class WearDataSender(private val context: Context) {
    private val messageClient by lazy { Wearable.getMessageClient(context) }
    private val nodeClient by lazy { Wearable.getNodeClient(context) }
    
    // Un solo publicador MQTT persistente
    private val mqttPublisher = MqttWearPublisher(context).apply { connect() }

    suspend fun enviarFC(bpm: Int) {
        // ACTUALIZAR REPOSITORIO LOCAL (Para que la UI del reloj cambie)
        SmartHealthRepository.actualizarFC(bpm)
        
        try {
            // 1. Enviar vía Wearable Data Layer (Bluetooth al teléfono si está conectado)
            val nodes: List<Node> = nodeClient.connectedNodes.await()
            val data = bpm.toString().toByteArray()
            
            nodes.forEach { node ->
                messageClient.sendMessage(node.id, "/smarthealthmonitor/fc", data).await()
            }
            Log.d("WearDataSender", "FC enviada vía Bluetooth: $bpm bpm")

            // 2. Publicar vía MQTT para HiveMQ Cloud (Directo a la Nube)
            val estado = when {
                bpm < 60 -> "FC Baja"
                bpm > 100 -> "FC Alta"
                else -> "Normal"
            }
            mqttPublisher.publishFC(bpm, estado)

        } catch (e: Exception) {
            Log.e("WearDataSender", "Error al enviar FC", e)
        }
    }
}
