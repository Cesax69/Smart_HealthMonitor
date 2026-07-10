package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import mx.utng.smarthealthmonitor.shared.MockData
import mx.utng.smarthealthmonitor.shared.data.LecturaFC

/**
 * MainFragment para Android TV usando Leanback.
 * Implementa una cuadrícula de filas con tarjetas de salud.
 */
class MainFragment : BrowseSupportFragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        cargarFilas()
    }

    private fun setupUI() {
        title = "SmartHealth TV"
        
        // Color de la marca en el sidebar (sh_primary)
        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)
        
        // Color de la barra de búsqueda (sh_amber)
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.sh_amber)
        
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (FC + Pasos) ───────────
        val estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        // Datos simulados para el ejercicio
        estadoAdapter.add(LecturaFC(id=0, valorBpm=88, timestamp=0L, hora="Ahora", esNormal=true))
        estadoAdapter.add(LecturaFC(id=1, valorBpm=4250, timestamp=0L, hora="Pasos", esNormal=true))
        rowsAdapter.add(ListRow(HeaderItem(0, "Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC ────────────────────
        val histAdapter = ArrayObjectAdapter(FCCardPresenter())
        // Usar datos del MockData centralizado
        MockData.historialFC.forEach { registro ->
            histAdapter.add(LecturaFC(
                id = registro.id,
                valorBpm = registro.bpm,
                timestamp = 0L,
                hora = registro.fecha,
                esNormal = registro.esNormal
            ))
        }
        rowsAdapter.add(ListRow(HeaderItem(1, "Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }
}
