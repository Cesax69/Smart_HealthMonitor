package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.shared.data.LecturaFC
import mx.utng.smarthealthmonitor.shared.MockData

class MainFragment : BrowseSupportFragment() {
    
    private val TAG = "SmartHealthTV"
    private val viewModel: TvViewModel by viewModels()
    private lateinit var rowsAdapter: ArrayObjectAdapter
    private lateinit var histAdapter: ArrayObjectAdapter
    private lateinit var estadoAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Fragment onCreate")
        setupUI()
        setupAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Fragment onViewCreated")
        cargarDatosSimulados()
        observarDatosReales()
    }

    private fun setupUI() {
        title = "SmartHealth TV"
        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
    }

    private fun setupAdapter() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        
        // Fila 1: Estado
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(0, "Estado actual"), estadoAdapter))

        // Fila 2: Historial
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(1, "Historial FC"), histAdapter))
        
        adapter = rowsAdapter
    }

    private fun cargarDatosSimulados() {
        Log.d(TAG, "Cargando datos simulados")
        // Agregar algo inmediatamente para ver si renderiza
        estadoAdapter.add(LecturaFC(id=-1, valorBpm=75, timestamp=0L, hora="Ahora", esNormal=true))
        estadoAdapter.add(LecturaFC(id=-2, valorBpm=4250, timestamp=0L, hora="Pasos", esNormal=true))
        
        // Cargar MockData
        try {
            val lista = MockData.historialFC
            for (i in lista.indices) {
                val registro = lista[i]
                histAdapter.add(LecturaFC(
                    id = registro.id,
                    valorBpm = registro.bpm,
                    timestamp = 0L,
                    hora = registro.fecha,
                    esNormal = registro.esNormal
                ))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cargando mock data: ${e.message}")
        }
    }

    private fun observarDatosReales() {
        Log.d(TAG, "Iniciando observación de Room")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    Log.d(TAG, "Recibidas ${lecturas.size} lecturas de Room")
                    if (lecturas.isNotEmpty()) {
                        histAdapter.clear()
                        for (i in lecturas.indices) {
                            histAdapter.add(lecturas[i])
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fc.collect { valor ->
                    if (valor > 0 && estadoAdapter.size() > 0) {
                        val current = LecturaFC(id=-1, valorBpm=valor, timestamp=0L, hora="Ahora", esNormal=valor in 60..100)
                        estadoAdapter.replace(0, current)
                    }
                }
            }
        }
    }
}
