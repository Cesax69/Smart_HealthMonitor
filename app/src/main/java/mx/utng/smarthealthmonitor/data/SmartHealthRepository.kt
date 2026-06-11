package mx.utng.smarthealthmonitor.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB
import mx.utng.smarthealthmonitor.shared.data.LecturaFC
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository as SharedRepo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SmartHealthRepository {
    private val dao: LecturaFCDao by lazy { SmartHealthDB.obtenerInstancia().lecturaDao() }
    
    val fcFlow = SharedRepo.fc
    val pasosFlow = SharedRepo.pasos
    val spo2Flow = SharedRepo.spo2

    fun obtenerHistorial(): Flow<List<LecturaFC>> {
        return dao.getAll().map { listaEntities ->
            listaEntities.map { entity ->
                LecturaFC(
                    id = entity.id,
                    valorBpm = entity.valorBpm,
                    timestamp = entity.timestamp,
                    hora = entity.hora,
                    esNormal = entity.esNormal
                )
            }
        }
    }

    suspend fun actualizarFC(bpm: Int) {
        SharedRepo.actualizarFC(bpm)
        val lectura = mx.utng.smarthealthmonitor.data.db.LecturaFC(
            valorBpm = bpm,
            timestamp = System.currentTimeMillis(),
            hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            esNormal = bpm in 60..100
        )
        dao.insert(lectura)
    }

    fun actualizarPasos(p: Int) = SharedRepo.actualizarPasos(p)
    fun actualizarSpO2(s: Int) = SharedRepo.actualizarSpO2(s)
}
