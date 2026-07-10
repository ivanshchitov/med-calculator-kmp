package org.dishch.medcalculator.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dosage_regimens_full",
    foreignKeys = [
        ForeignKey(
            entity = MedicationFullEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medication_id"])]
)
data class DosageRegimenFullEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "medication_id") val medicationId: String,
    @ColumnInfo(name = "route") val route: String,
    @ColumnInfo(name = "from_months") val fromMonths: Int,
    @ColumnInfo(name = "to_months") val toMonths: Int,
    @ColumnInfo(name = "from_kg") val fromKg: Double?,
    @ColumnInfo(name = "to_kg") val toKg: Double?,
    @ColumnInfo(name = "dose_min") val doseMin: Double?,
    @ColumnInfo(name = "dose_max") val doseMax: Double?,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "max_dose_mg") val maxDoseMg: Double?,
    @ColumnInfo(name = "note") val note: String?
)
