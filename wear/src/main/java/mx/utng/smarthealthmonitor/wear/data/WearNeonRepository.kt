package mx.utng.smarthealthmonitor.wear.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.shared.data.remote.NeonClient
import mx.utng.smarthealthmonitor.shared.data.remote.NeonRequest

class WearNeonRepository {
    suspend fun publicarLecturaNeon(bpm: Int, estado: String, hora: String) {
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = "INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora) VALUES ($1, $2, 'wear', $3)",
                    params = listOf(bpm, estado, hora)
                )
            )
        }
    }
}
