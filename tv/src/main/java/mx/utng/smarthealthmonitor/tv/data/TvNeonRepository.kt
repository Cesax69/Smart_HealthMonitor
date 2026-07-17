package mx.utng.smarthealthmonitor.tv.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.shared.data.remote.NeonClient
import mx.utng.smarthealthmonitor.shared.data.remote.NeonRequest
import mx.utng.smarthealthmonitor.shared.data.remote.LecturaFcDto
import android.util.Log

class TvNeonRepository {

    /** Obtener historial completo de los 3 dispositivos */
    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                NeonClient.api.executeQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """SELECT id, bpm, estado, dispositivo, hora, fecha, created_at
                                   FROM lecturas_fc
                                   ORDER BY created_at DESC
                                   LIMIT $1""".trimIndent(),
                        params = listOf(limite)
                    )
                ).rows
            } catch (e: Exception) {
                Log.e("TvNeonRepo", "Error historial", e)
                emptyList()
            }
        }
 
    /** Estadísticas por dispositivo */
    suspend fun obtenerEstadisticas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                NeonClient.api.executeQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """SELECT 0 as id, ROUND(AVG(bpm)) AS bpm,
                                   'Promedio' AS estado, dispositivo,
                                   MAX(hora) AS hora, '' as fecha, '' as created_at
                                   FROM lecturas_fc
                                   GROUP BY dispositivo""".trimIndent()
                    )
                ).rows
            } catch (e: Exception) {
                Log.e("TvNeonRepo", "Error estadisticas", e)
                emptyList()
            }
        }

    // --- Consultas Avanzadas ---

    suspend fun obtenerAlertas(limite: Int = 10): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                NeonClient.api.executeQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """SELECT id, bpm, estado, dispositivo, hora, fecha, created_at FROM lecturas_fc
                                    WHERE (bpm < 60 OR bpm > 100)
                                      AND created_at > NOW() - INTERVAL '24 hours'
                                    ORDER BY created_at DESC LIMIT $1""".trimIndent(),
                        params = listOf(limite)
                    )
                ).rows
            } catch (e: Exception) {
                Log.e("TvNeonRepo", "Error alertas", e)
                emptyList()
            }
        }
        
    suspend fun obtenerPromedioPorHora(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                NeonClient.api.executeQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """SELECT 0 as id, ROUND(AVG(bpm)) AS bpm,
                                   'Promedio' AS estado, 'general' as dispositivo,
                                   EXTRACT(HOUR FROM created_at)::text AS hora, '' as fecha, '' as created_at
                                   FROM lecturas_fc
                                   GROUP BY EXTRACT(HOUR FROM created_at)
                                   ORDER BY EXTRACT(HOUR FROM created_at)""".trimIndent()
                    )
                ).rows
            } catch (e: Exception) {
                Log.e("TvNeonRepo", "Error promedio por hora", e)
                emptyList()
            }
        }
        
    suspend fun obtenerLecturaMasRecientePorDispositivo(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                NeonClient.api.executeQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """SELECT DISTINCT ON (dispositivo)
                                   id, bpm, estado, dispositivo, hora, fecha, created_at
                                   FROM lecturas_fc
                                   ORDER BY dispositivo, created_at DESC""".trimIndent()
                    )
                ).rows
            } catch (e: Exception) {
                Log.e("TvNeonRepo", "Error lecturas recientes", e)
                emptyList()
            }
        }
        
    suspend fun obtenerTaquicardiaSostenida(): Int =
        withContext(Dispatchers.IO) {
            try {
                val res = NeonClient.api.executeGenericQuery(
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query  = """SELECT COUNT(*) AS lecturas_altas
                                   FROM lecturas_fc
                                   WHERE bpm > 100
                                     AND created_at > NOW() - INTERVAL '1 hour'""".trimIndent()
                    )
                )
                val rows = res.getAsJsonArray("rows")
                if (rows.size() > 0) {
                    rows[0].asJsonObject.get("lecturas_altas").asInt
                } else 0
            } catch (e: Exception) {
                Log.e("TvNeonRepo", "Error taquicardia", e)
                0
            }
        }
}
