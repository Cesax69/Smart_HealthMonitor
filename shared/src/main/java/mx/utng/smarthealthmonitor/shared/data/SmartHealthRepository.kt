package mx.utng.smarthealthmonitor.shared.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.utng.smarthealthmonitor.shared.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.shared.data.db.SmartHealthDB
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * REPOSITORIO ÚNICO Y CENTRALIZADO (Usa este siempre)
 */
object SmartHealthRepository {
    private var _dao: LecturaFCDao? = null
    
    // Flujos en memoria para reactividad instantánea
    private val _fc = MutableStateFlow(72)
    val fcFlow: StateFlow<Int> = _fc.asStateFlow()

    private val _pasos = MutableStateFlow(2500)
    val pasosFlow: StateFlow<Int> = _pasos.asStateFlow()

    private val _spo2 = MutableStateFlow(98)
    val spo2Flow: StateFlow<Int> = _spo2.asStateFlow()

    // Compatibilidad con código anterior
    val fc: StateFlow<Int> get() = fcFlow
    val pasos: StateFlow<Int> get() = pasosFlow
    val historial: StateFlow<List<LecturaFC>> = MutableStateFlow(emptyList()) // Solo para evitar errores de compilación, se debe usar obtenerHistorial()

    fun init(context: Context) {
        if (_dao == null) {
            _dao = SmartHealthDB.getDatabase(context).lecturaDao()
        }
    }

    private val dao: LecturaFCDao 
        get() = _dao ?: throw IllegalStateException("Repository not initialized. Call init(context) first.")

    fun obtenerHistorial(): Flow<List<LecturaFC>> {
        return dao.obtenerTodas()
    }

    suspend fun actualizarFC(bpm: Int) {
        // 1. Actualizar flujo en memoria
        _fc.value = bpm
        
        // 2. Persistir en base de datos Room usando SyncRepository
        val lectura = LecturaFC(
            bpm = bpm,
            estado = if (bpm > 100) "FC Alta" else "Normal",
            dispositivo = "app",
            hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            sincronizado = false
        )
        try {
            mx.utng.smarthealthmonitor.shared.data.repository.SyncRepository(dao).insertarLectura(lectura)
        } catch (e: Exception) {
            // Error al insertar
        }
    }

    fun actualizarPasos(p: Int) { _pasos.value = p }
    fun actualizarSpO2(s: Int) { _spo2.value = s }
}
