package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.shared.data.repository.SmartHealthRepository
import mx.utng.smarthealthmonitor.shared.data.LecturaFC

/**
 * Estado de la UI para la televisión.
 */
data class TvUiState(
    val fc: Int = 0,
    val lecturas: List<LecturaFC> = emptyList()
)

class TvViewModel : ViewModel() {

    // Combinar el flujo de FC actual y el historial en un solo estado para Compose
    val state: StateFlow<TvUiState> = combine(
        SmartHealthRepository.fcFlow,
        SmartHealthRepository.obtenerHistorial()
    ) { fcValue, historialList ->
        TvUiState(fcValue, historialList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TvUiState()
    )
    
    // Flujos individuales para compatibilidad
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val historial: StateFlow<List<LecturaFC>> = SmartHealthRepository.obtenerHistorial()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

/**
 * Factory para crear el TvViewModel.
 */
class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TvViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
