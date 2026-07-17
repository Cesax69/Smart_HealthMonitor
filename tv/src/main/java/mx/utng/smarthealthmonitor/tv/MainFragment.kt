package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
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

    private val viewModel: TvViewModel by viewModels()
    private lateinit var histAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Configuración visual
        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)

        cargarFilas()
        observarDatos()
    }

    private fun observarDatos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { uiState ->
                    histAdapter.clear()
                    val lecturas = uiState.lecturas
                    if (lecturas.isEmpty()) {
                        // SI NO HAY DATOS EN ROOM, MOSTRAR MOCKDATA PARA LA EVIDENCIA
                        MockData.historialFC.forEach { registro ->
                            histAdapter.add(LecturaFC(
                                id = registro.id,
                                bpm = registro.bpm,
                                estado = if (registro.esNormal) "Normal" else "FC Alta",
                                hora = registro.fecha
                            ))
                        }
                    } else {
                        // SI HAY DATOS REALES, MOSTRARLOS
                        lecturas.forEach { histAdapter.add(it) }
                    }
                }
            }
        }
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // Fila 1: Historial de salud
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(0, "Historial de Salud"), histAdapter))

        // Fila 2: Resumen (Datos fijos para asegurar que se vea algo)
        val resumenAdapter = ArrayObjectAdapter(FCCardPresenter())
        resumenAdapter.add(LecturaFC(id=-1, bpm=72, estado="Normal", hora="Frecuencia Promedio"))
        resumenAdapter.add(LecturaFC(id=-2, bpm=4500, estado="Normal", hora="Pasos Totales"))
        rowsAdapter.add(ListRow(HeaderItem(1, "Resumen Diario"), resumenAdapter))

        this.adapter = rowsAdapter
    }
}
