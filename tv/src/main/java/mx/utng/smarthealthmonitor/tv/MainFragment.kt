package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment

/**
 * MainFragment para Android TV usando Leanback.
 */
class MainFragment : BrowseSupportFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title = "SmartHealth TV"
    }
}
