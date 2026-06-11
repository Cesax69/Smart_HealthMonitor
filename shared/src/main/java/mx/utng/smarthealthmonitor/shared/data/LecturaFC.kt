package mx.utng.smarthealthmonitor.shared.data

data class LecturaFC(
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long,
    val hora: String,
    val esNormal: Boolean
)
