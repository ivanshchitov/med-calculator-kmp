package org.dishch.medcalculator.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications_full")
data class MedicationFullEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name_ru") val nameRu: String,
    @ColumnInfo(name = "name_latin") val nameLatin: String,
    @ColumnInfo(name = "dosage_mg_per_ml") val dosageMgPerMl: Double,
    @ColumnInfo(name = "min_age_months") val minAgeMonths: Int,
    @ColumnInfo(name = "max_single_dose_mg") val maxSingleDoseMg: Double?
)
