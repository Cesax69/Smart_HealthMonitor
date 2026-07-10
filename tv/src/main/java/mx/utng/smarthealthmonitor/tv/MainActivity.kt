package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * MainActivity para Android TV.
 * Es solo el contenedor: carga MainFragment.
 */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Cargar el fragmento solo si es la primera vez que se crea la actividad
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }
    }
}
