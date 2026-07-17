package mx.utng.smarthealthmonitor.data.sync

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.runBlocking
import mx.utng.smarthealthmonitor.shared.data.db.SmartHealthDB
import mx.utng.smarthealthmonitor.shared.data.repository.SyncRepository
import java.util.concurrent.TimeUnit
 
class NeonSyncWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {
 
    override suspend fun doWork(): Result {
        return try {
            val db   = SmartHealthDB.getDatabase(applicationContext)
            val repo = SyncRepository(db.lecturaDao())
 
            // 1. Enviar pendientes locales a Neon
            repo.enviarPendientes()
 
            // 2. Descargar los más recientes de Neon
            repo.sincronizarDesdeNeon(limite = 100)
 
            android.util.Log.d("SYNC_WORKER","✅ Sync completado")
            Result.success()
        } catch (e: Exception) {
            android.util.Log.e("SYNC_WORKER","❌ Sync fallido: ${e.message}")
            Result.retry()   // WorkManager reintentará automáticamente
        }
    }
 
    companion object {
        const val WORK_NAME = "NeonSyncWork"
 
        /** Programar sync periódico cada 30 minutos y uno inmediato */
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
 
            // 1. Sincronización inmediata (útil para pruebas)
            val immediateRequest = OneTimeWorkRequestBuilder<NeonSyncWorker>()
                .setConstraints(constraints)
                .build()
                
            WorkManager.getInstance(context).enqueue(immediateRequest)

            // 2. Sincronización periódica (cada 30 min)
            val periodicRequest = PeriodicWorkRequestBuilder<NeonSyncWorker>(
                30, TimeUnit.MINUTES
            ).setConstraints(constraints)
             .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5, TimeUnit.MINUTES)
             .build()
 
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        }
    }
}
