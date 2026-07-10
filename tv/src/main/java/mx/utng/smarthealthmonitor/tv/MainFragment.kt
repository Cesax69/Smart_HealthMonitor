package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment

/**
 * MainFragment para Android TV usando Leanback.
 */
class MainFragment : BrowseSupportFragment() {
    
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        setupUI()
    }

    private fun setupUI() {
        title = "SmartHealth TV"
        
        // FORZAR COLORES MD3 PROGRAMÁTICAMENTE
        // Color del encabezado/marca (sh_primary)
        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)
        
        // Color de la barra de búsqueda (sh_amber)
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.sh_amber)
        
        // El fondo se controla mejor desde el tema, pero podemos asegurar el modo
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
    }
}
