package org.dishch.medcalculator.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dosage_regimens",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medication_id"])]
)
data class DosageRegimenEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "from_age") val fromAge: Int,
    @ColumnInfo(name = "to_age") val toAge: Int,
    @ColumnInfo(name = "min_dose_per_kg") val minDosePerKg: Double,
    @ColumnInfo(name = "max_dose_per_kg") val maxDosePerKg: Double,
    @ColumnInfo(name = "medication_id") val medicationId: Long
)
