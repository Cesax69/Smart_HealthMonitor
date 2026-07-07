package mx.utng.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import mx.utng.smarthealthmonitor.shared.data.LecturaFC
import mx.utng.smarthealthmonitor.shared.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.shared.data.db.SmartHealthDB
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository as SharedRepo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SmartHealthRepository {
    private var _dao: LecturaFCDao? = null
    
    fun init(context: Context) {
        if (_dao == null) {
            _dao = SmartHealthDB.getDatabase(context).lecturaDao()
        }
    }

    private val dao: LecturaFCDao 
        get() = _dao ?: throw IllegalStateException("Repository not initialized. Call init(context) first.")
    
    val fcFlow = SharedRepo.fc
    val pasosFlow = SharedRepo.pasos
    val spo2Flow = SharedRepo.spo2

    fun obtenerHistorial(): Flow<List<LecturaFC>> {
        return dao.getAll()
    }

    suspend fun actualizarFC(bpm: Int) {
        SharedRepo.actualizarFC(bpm)
        val lectura = LecturaFC(
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
