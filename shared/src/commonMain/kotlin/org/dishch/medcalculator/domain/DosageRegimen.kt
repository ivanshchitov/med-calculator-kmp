package org.dishch.medcalculator.domain

import org.dishch.medcalculator.formattedDouble

data class DosageRegimen(
    val id: Long,
    val fromAge: Int,
    val toAge: Int,
    val minDosePerKg: Double,
    val maxDosePerKg: Double,
    val medicationId: Long
)

val DosageRegimen.formattedMinDose: String
    get() = formattedDouble(minDosePerKg)

val DosageRegimen.formattedMaxDose: String
    get() = formattedDouble(maxDosePerKg)

val DosageRegimen.formattedDoseRange: String
    get() = "$formattedMinDose-$formattedMaxDose"