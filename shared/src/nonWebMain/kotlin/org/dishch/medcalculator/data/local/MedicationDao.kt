package org.dishch.medcalculator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedications(medications: List<MedicationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDosageRegimens(regimens: List<DosageRegimenEntity>)

    @Query("DELETE FROM medications")
    suspend fun clearMedications()

    @Query("DELETE FROM dosage_regimens")
    suspend fun clearDosageRegimens()

    @Transaction
    suspend fun replaceMedicationData(
        medications: List<MedicationEntity>,
        regimens: List<DosageRegimenEntity>
    ) {
        clearMedications()
        clearDosageRegimens()
        insertMedications(medications)
        insertDosageRegimens(regimens)
    }

    @Query("SELECT * FROM medications WHERE id = :medicationId")
    fun getMedicationById(medicationId: String): Flow<MedicationEntity?>

    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM dosage_regimens WHERE medication_id = :medicationId")
    fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimenEntity>>
}
