package mx.utng.smarthealthmonitor.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.shared.data.LecturaFC
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.wear.data.HealthDataService
import mx.utng.smarthealthmonitor.wear.data.WearDataSender

class WearDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dataSender = WearDataSender(application)

    init {
        // ACTIVAR EL SENSOR DE SALUD EN EL INICIO
        viewModelScope.launch {
            try {
                HealthDataService.registrar(application)
            } catch (e: Exception) {
                // Error silencioso en emuladores sin Health Services
            }
        }
    }

    // Ritmo cardíaco desde el módulo shared
    val fc: StateFlow<Int> = SmartHealthRepository.fc
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72
        )

    /**
     * MÉTODO DE EMERGENCIA: Simular un pulso desde la UI del reloj
     * Esto disparará el flujo MQTT aunque el sensor del emulador falle.
     */
    fun simularPulso() {
        viewModelScope.launch {
            val bpmAleatorio = (60..120).random()
            dataSender.enviarFC(bpmAleatorio)
        }
    }

    // Conteo de pasos desde el módulo shared
    val pasos: StateFlow<Int> = SmartHealthRepository.pasos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    // Historial de lecturas desde el módulo shared
    val historial: StateFlow<List<LecturaFC>> = SmartHealthRepository.historial
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
