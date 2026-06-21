package org.dishch.medcalculator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DosageRegimenDao {
    @Query("SELECT * FROM dosage_regimens WHERE medication_id = :medicationId")
    fun getRegimensForMedication(medicationId: Long): Flow<List<DosageRegimenEntity>>

    @Insert
    suspend fun insert(regimen: DosageRegimenEntity): Long

    @Delete
    suspend fun delete(regimen: DosageRegimenEntity)
}
