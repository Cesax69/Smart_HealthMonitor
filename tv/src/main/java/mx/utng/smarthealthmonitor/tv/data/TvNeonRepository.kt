package mx.utng.smarthealthmonitor.tv.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.shared.data.remote.NeonClient
import mx.utng.smarthealthmonitor.shared.data.remote.NeonRequest
import mx.utng.smarthealthmonitor.shared.data.remote.LecturaFcDto
import android.util.Log

class TvNeonRepository {

    suspend fun obtenerHistorialCompleto(): List<LecturaFcDto> = withContext(Dispatchers.IO) {
        try {
            val res = NeonClient.api.executeQuery(
                NeonClient.AUTH_HEADER, NeonClient.CONN_STRING,
                NeonRequest("SELECT * FROM lecturas_fc ORDER BY fecha DESC, hora DESC LIMIT 100")
            )
            res.rows
        } catch (e: Exception) {
            Log.e("TvNeonRepo", "Error al obtener historial", e)
            emptyList()
        }
    }

    suspend fun obtenerPromedioBpmDelDia(): Double = withContext(Dispatchers.IO) {
        try {
            val res = NeonClient.api.executeGenericQuery(
                NeonClient.AUTH_HEADER, NeonClient.CONN_STRING,
                NeonRequest("SELECT AVG(bpm) as promedio FROM lecturas_fc WHERE fecha = CURRENT_DATE")
            )
            val rows = res.getAsJsonArray("rows")
            if (rows.size() > 0 && !rows[0].asJsonObject.get("promedio").isJsonNull) {
                rows[0].asJsonObject.get("promedio").asDouble
            } else 0.0
        } catch (e: Exception) {
            Log.e("TvNeonRepo", "Error promedio", e)
            0.0
        }
    }

    suspend fun obtenerPicosDeEstres(): Int = withContext(Dispatchers.IO) {
        try {
            val res = NeonClient.api.executeGenericQuery(
                NeonClient.AUTH_HEADER, NeonClient.CONN_STRING,
                NeonRequest("SELECT COUNT(*) as picos FROM lecturas_fc WHERE bpm > 100 AND estado = 'FC Alta'")
            )
            val rows = res.getAsJsonArray("rows")
            if (rows.size() > 0) {
                rows[0].asJsonObject.get("picos").asInt
            } else 0
        } catch (e: Exception) {
            Log.e("TvNeonRepo", "Error picos", e)
            0
        }
    }
}
