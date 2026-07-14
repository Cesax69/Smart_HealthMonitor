package mx.utng.smarthealthmonitor.shared.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SmartHealthRepository {
    private val _fc = MutableStateFlow(72)
    val fc: MutableStateFlow<Int> = _fc

    private val _pasos = MutableStateFlow(2500)
    val pasos: MutableStateFlow<Int> = _pasos

    private val _spo2 = MutableStateFlow(98)
    val spo2: MutableStateFlow<Int> = _spo2

    private val _historial = MutableStateFlow<List<LecturaFC>>(
        listOf(
            LecturaFC(1, 75, 1000L, "10:00", true),
            LecturaFC(2, 125, 2000L, "10:15", false),
            LecturaFC(3, 80, 3000L, "10:30", true),
            LecturaFC(4, 72, 4000L, "10:45", true),
            LecturaFC(5, 130, 5000L, "11:00", false)
        )
    )
    val historial: StateFlow<List<LecturaFC>> = _historial.asStateFlow()

    fun actualizarFC(valor: Int) {
        _fc.value = valor
    }

    fun actualizarPasos(valor: Int) {
        _pasos.value = valor
    }

    fun actualizarSpO2(valor: Int) {
        _spo2.value = valor
    }

    fun agregarLectura(lectura: LecturaFC) {
        _historial.value = listOf(lectura) + _historial.value
    }
    
    fun setHistorial(lecturas: List<LecturaFC>) {
        _historial.value = lecturas
    }
}
