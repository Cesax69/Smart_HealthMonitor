package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository

class WearDashboardViewModel : ViewModel() {

    // Reutiliza el mismo Repository del módulo shared
    val fc: StateFlow<Int> = SmartHealthRepository.fc
        .map { if (it == 0) 72 else it }  // valor por defecto
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72
        )
}
