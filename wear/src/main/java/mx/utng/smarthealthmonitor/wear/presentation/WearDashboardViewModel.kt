package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository

class WearDashboardViewModel : ViewModel() {

    // Ritmo cardíaco desde el módulo shared
    val fc: StateFlow<Int> = SmartHealthRepository.fc
        .map { if (it == 0) 72 else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72
        )

    // Conteo de pasos desde el módulo shared (Reto Adicional)
    val pasos: StateFlow<Int> = SmartHealthRepository.pasos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    // Historial de lecturas desde el módulo shared
    val historial = SmartHealthRepository.historial
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
