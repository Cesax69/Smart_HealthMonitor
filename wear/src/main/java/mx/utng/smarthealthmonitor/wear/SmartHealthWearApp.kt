package mx.utng.smarthealthmonitor.wear

import android.app.Application
import mx.utng.smarthealthmonitor.shared.data.SmartHealthRepository

class SmartHealthWearApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            SmartHealthRepository.init(this)
        } catch (e: Exception) {
            android.util.Log.e("WEAR_APP", "Error init repo: ${e.message}")
        }
    }
}
