package mx.utng.smarthealthmonitor.shared.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id           : Int     = 0,
    val bpm          : Int,
    val estado       : String,
    val dispositivo  : String  = "app",  // wear | app | tv
    val hora         : String,
    @ColumnInfo(name = "sincronizado")
    val sincronizado : Boolean = false   // false = pendiente de sync
) {
    // Propiedades calculadas para retrocompatibilidad con la UI anterior
    val valorBpm: Int get() = bpm
    val esNormal: Boolean get() = estado != "FC Alta" && estado != "FC_ALTA"
    val timestamp: Long get() = System.currentTimeMillis() // Dummy value since timestamp was removed
}
