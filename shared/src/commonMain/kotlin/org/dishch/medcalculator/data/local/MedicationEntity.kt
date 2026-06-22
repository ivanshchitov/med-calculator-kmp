package org.dishch.medcalculator.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "dosage") val dosage: Double,
    @ColumnInfo(name = "max_single_dose") val maxSingleDose: Double,
    @ColumnInfo(name = "age_limit", defaultValue = "0") val ageLimit: Int = 0
)
