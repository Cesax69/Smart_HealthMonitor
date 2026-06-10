package mx.utng.smarthealthmonitor.shared.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SmartHealthRepository {
    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    private val _pasos = MutableStateFlow(2500)
    val pasos: StateFlow<Int> = _pasos.asStateFlow()

    private val _spo2 = MutableStateFlow(98)
    val spo2: StateFlow<Int> = _spo2.asStateFlow()

    private val _historial = MutableStateFlow<List<LecturaFC>>(emptyList())
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
