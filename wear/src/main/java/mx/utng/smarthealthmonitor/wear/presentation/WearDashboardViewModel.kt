package mx.utng.smarthealthmonitor.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.shared.data.LecturaFC
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.wear.data.HealthDataService

class WearDashboardViewModel(application: Application) : AndroidViewModel(application) {

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
