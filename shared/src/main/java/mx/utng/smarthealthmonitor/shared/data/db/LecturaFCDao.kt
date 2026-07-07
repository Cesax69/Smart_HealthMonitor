package mx.utng.smarthealthmonitor.shared.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.utng.smarthealthmonitor.shared.data.LecturaFC

@Dao
interface LecturaFCDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lectura: LecturaFC)

    @Query("SELECT * FROM lecturas_fc ORDER BY timestamp DESC")
    fun getAll(): Flow<List<LecturaFC>>

    @Query("SELECT COUNT(*) FROM lecturas_fc")
    suspend fun count(): Int

    @Query("DELETE FROM lecturas_fc WHERE timestamp < :limit")
    suspend fun cleanOld(limit: Long)
}
