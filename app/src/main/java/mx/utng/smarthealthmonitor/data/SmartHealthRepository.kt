package mx.utng.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB

/**
 * Repositorio singleton que centraliza los datos de salud.
 * El WearListenerService escribe aquí.
 * El ViewModel lee de aquí.
 */
object SmartHealthRepository {

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()
    
    // SpO2 (saturación de oxígeno, %) - Reto adicional
    private val _spo2Flow = MutableStateFlow(0)
    val spo2Flow: StateFlow<Int> = _spo2Flow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Persistir en Room automáticamente
        dao?.insertar(LecturaFC(valorBpm = bpm))
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
    
    fun actualizarSpO2(pct: Int) {
        _spo2Flow.value = pct
    }

    // Flow del historial desde Room
    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}
