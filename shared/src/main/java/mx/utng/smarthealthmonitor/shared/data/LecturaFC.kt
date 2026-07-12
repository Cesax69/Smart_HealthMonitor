package mx.utng.smarthealthmonitor.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long,
    val hora: String,
    val esNormal: Boolean
)
