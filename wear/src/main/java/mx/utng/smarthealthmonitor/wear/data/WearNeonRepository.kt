package mx.utng.smarthealthmonitor.wear.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.shared.data.remote.NeonClient
import mx.utng.smarthealthmonitor.shared.data.remote.NeonRequest
import mx.utng.smarthealthmonitor.shared.data.remote.LecturaFcDto

class WearNeonRepository {
    suspend fun publicarLecturaNeon(bpm: Int, estado: String, hora: String) {
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = "INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora) VALUES ($1, $2, 'wear', $3)",
                    params = listOf(bpm, estado, hora)
                )
            )
        }
    }
    
    suspend fun obtenerUltimasLecturas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                NeonClient.api.executeQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = "SELECT id, bpm, estado, dispositivo, hora, fecha, created_at FROM lecturas_fc WHERE dispositivo='wear' ORDER BY created_at DESC LIMIT 5",
                    )
                ).rows
            } catch (e: Exception) {
                emptyList()
            }
        }
}
