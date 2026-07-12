package org.dishch.medcalculator.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.dishch.medcalculator.domain.model.DosageRegimen

enum class Route { IV, IM, SC }
enum class Unit { MG, MG_PER_KG }

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
    @ColumnInfo(name = "medication_id") val medicationId: String,
    @ColumnInfo(name = "route") val route: Route,
    @ColumnInfo(name = "from_months") val fromMonths: Int,
    @ColumnInfo(name = "to_months") val toMonths: Int,
    @ColumnInfo(name = "from_kg") val fromKg: Double?,
    @ColumnInfo(name = "to_kg") val toKg: Double?,
    @ColumnInfo(name = "dose_min") val doseMin: Double?,
    @ColumnInfo(name = "dose_max") val doseMax: Double?,
    @ColumnInfo(name = "unit") val unit: Unit,
    @ColumnInfo(name = "max_dose_mg") val maxDoseMg: Double?,
    @ColumnInfo(name = "note") val note: String?
)

fun DosageRegimenEntity.toDomain(): DosageRegimen =
    DosageRegimen(
        id = id,
        fromMonths = fromMonths,
        toMonths = toMonths,
        fromKg = fromKg,
        toKg = toKg,
        minDose = doseMin,
        maxDose = doseMax,
        maxDoseMg = maxDoseMg,
        route = route.toDomain(),
        unit = unit.toDomain(),
        medicationId = medicationId,
    )