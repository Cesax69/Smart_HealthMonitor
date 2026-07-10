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

/**
 * MainFragment para Android TV usando Leanback.
 * Conectado reactivamente a Room y al Repositorio.
 */
class MainFragment : BrowseSupportFragment() {
    
    private val viewModel: TvViewModel by viewModels()
    private lateinit var histAdapter: ArrayObjectAdapter
    private lateinit var estadoAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        cargarFilas()
        observarDatos()
    }

    private fun setupUI() {
        title = "SmartHealth TV"
        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.sh_amber)
        
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
    }

    private fun observarDatos() {
        // Observar historial de Room y actualizar la fila automáticamente
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    lecturas.forEach { histAdapter.add(it) }
                }
            }
        }

        // Observar FC actual
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fc.collect { valor ->
                    // Actualizar la primera tarjeta de la fila de estado
                    if (estadoAdapter.size() > 0) {
                        val current = LecturaFC(id=-1, valorBpm=valor, timestamp=0L, hora="Ahora", esNormal=valor in 60..100)
                        estadoAdapter.replace(0, current)
                    }
                }
            }
        }
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (Reactiva) ─────────────
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        estadoAdapter.add(LecturaFC(id=-1, valorBpm=0, timestamp=0L, hora="Ahora", esNormal=true))
        estadoAdapter.add(LecturaFC(id=-2, valorBpm=4250, timestamp=0L, hora="Pasos", esNormal=true))
        rowsAdapter.add(ListRow(HeaderItem(0, "Estado actual"), estadoAdapter))

        // ── Fila 2: Historial (Reactiva) ────────────────
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(1, "Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }
}
