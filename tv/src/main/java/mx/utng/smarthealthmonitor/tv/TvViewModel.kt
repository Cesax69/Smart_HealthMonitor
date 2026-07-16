package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.mqtt.TvMessage
import mx.utng.smarthealthmonitor.shared.data.LecturaFC
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber

/**
 * Estado de la UI para la televisión.
 */
data class TvUiState(
    val fcActual: Int = 0,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "--:--",
    val isLoading: Boolean = true,
    val connectionStatus: String = "Desconectado",
    val lecturas: List<LecturaFC> = emptyList(),
    val promedioBpm: Double = 0.0,
    val picosEstres: Int = 0
)

class TvViewModel(private val context: Context) : ViewModel() {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()
 
    private val neonRepo = mx.utng.smarthealthmonitor.tv.data.TvNeonRepository()

    // Flow de mensajes MQTT entrantes
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow) { status ->
        _state.update { it.copy(connectionStatus = status) }
    }
 
    init {
        mqttSubscriber.connect()
 
        // Observar mensajes MQTT y actualizar el estado de la UI
        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                tvMsg ?: return@collect
                _state.update { it.copy(
                    fcActual = tvMsg.bpm,
                    fcEstado = tvMsg.estado,
                    ultimaHora = tvMsg.hora,
                    isLoading = false
                )}
            }
        }

        // Cargar historial
        viewModelScope.launch {
            SmartHealthRepository.obtenerHistorial().collect { list ->
                _state.update { it.copy(lecturas = list) }
            }
        }

        // Consultas avanzadas de Neon (Reto Extra)
        viewModelScope.launch {
            try {
                val promedio = neonRepo.obtenerPromedioBpmDelDia()
                val picos = neonRepo.obtenerPicosDeEstres()
                _state.update { it.copy(promedioBpm = promedio, picosEstres = picos) }
            } catch (e: Exception) {
                android.util.Log.e("TvViewModel", "Error cargando Neon", e)
            }
        }
    }
 
    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}

/**
 * Factory para crear el TvViewModel.
 */
class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TvViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
