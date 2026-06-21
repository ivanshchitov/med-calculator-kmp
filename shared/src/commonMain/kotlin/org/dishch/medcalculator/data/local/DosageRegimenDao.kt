package org.dishch.medcalculator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DosageRegimenDao {

    @Insert
    suspend fun insertAll(regimens: List<DosageRegimenEntity>)

    @Query("DELETE FROM dosage_regimens")
    suspend fun deleteAll()

    @Query("SELECT * FROM dosage_regimens WHERE medication_id = :medicationId")
    fun getRegimensForMedication(medicationId: Long): Flow<List<DosageRegimenEntity>>

}
