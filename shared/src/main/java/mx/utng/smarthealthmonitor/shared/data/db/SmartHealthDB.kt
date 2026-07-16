package mx.utng.smarthealthmonitor.shared.data.db

import android.content.Context
import androidx.room.*
import mx.utng.smarthealthmonitor.shared.data.LecturaFC

@Database(
    entities = [LecturaFC::class],
    version = 2,
    exportSchema = false
)
abstract class SmartHealthDB : RoomDatabase() {
    abstract fun lecturaDao(): LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDB? = null

        fun getDatabase(context: Context): SmartHealthDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDB::class.java,
                    "smarthealthmonitor_db"
                )
                .fallbackToDestructiveMigration() // Evitar cierres por cambios de esquema
                .build().also { INSTANCE = it }
            }
        }
    }
}
