package org.dishch.medcalculator.data.local

object DataImportConverter {

    fun convert(json: MedicationsDataJson): Pair<List<MedicationEntity>, List<DosageRegimenEntity>> {
        val medications = json.medications.map { medication ->
            MedicationEntity(
                id = medication.id,
                nameRu = medication.nameRu,
                nameLatin = medication.nameLatin,
                dosageMgPerMl = medication.dosageMgPerMl,
                minAgeMonths = medication.minAgeMonths,
                maxSingleDoseMg = medication.maxSingleDoseMg
            )
        }

        val regimens = json.medications.flatMap { medication ->
            medication.dosageRegimens.flatMap { regimen ->
                regimen.rules.map { rule ->
                    DosageRegimenEntity(
                        medicationId = medication.id,
                        route = Route.valueOf(regimen.route),
                        fromMonths = rule.fromMonths,
                        toMonths = rule.toMonths,
                        fromKg = rule.fromKg,
                        toKg = rule.toKg,
                        doseMin = rule.doseMin,
                        doseMax = rule.doseMax,
                        dosageUnit = DosageUnit.valueOf(rule.unit),
                        maxDoseMg = rule.maxDoseMg,
                        note = rule.note
                    )
                }
            }
        }

        return Pair(medications, regimens)
    }
}
