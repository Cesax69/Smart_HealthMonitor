package mx.utng.smarthealthmonitor.wear

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {
    private val messageClient by lazy { Wearable.getMessageClient(context) }
    private val nodeClient by lazy { Wearable.getNodeClient(context) }

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes: List<Node> = nodeClient.connectedNodes.await()
            val data = bpm.toString().toByteArray()
            
            nodes.forEach { node ->
                messageClient.sendMessage(
                    node.id,
                    "/smarthealthmonitor/fc",
                    data
                ).await()
                Log.d("WearDataSender", "FC enviada: $bpm bpm al nodo ${node.displayName}")
            }
        } catch (e: Exception) {
            Log.e("WearDataSender", "Error al enviar FC", e)
        }
    }
}
