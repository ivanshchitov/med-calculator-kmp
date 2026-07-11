package org.dishch.medcalculator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.domain.model.Medication

@Dao
interface MedicationFullDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedications(medications: List<MedicationFullEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDosageRegimens(regimens: List<DosageRegimenFullEntity>)

    @Query("DELETE FROM medications_full")
    suspend fun clearMedications()

    @Query("DELETE FROM dosage_regimens_full")
    suspend fun clearDosageRegimens()

    @Transaction
    suspend fun replaceMedicationData(
        medications: List<MedicationFullEntity>,
        regimens: List<DosageRegimenFullEntity>
    ) {
        clearMedications()
        clearDosageRegimens()
        insertMedications(medications)
        insertDosageRegimens(regimens)
    }

    @Query("SELECT * FROM medications_full WHERE id = :medicationId")
    fun getMedicationById(medicationId: String): Flow<MedicationFullEntity?>

    @Query("SELECT * FROM medications_full")
    fun getAllMedications(): Flow<List<MedicationFullEntity>>

    @Query("SELECT * FROM dosage_regimens_full WHERE medication_id = :medicationId")
    fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimenFullEntity>>
}
