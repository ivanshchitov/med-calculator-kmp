package org.dishch.medcalculator.data.local

object DataImportConverter {

    fun convert(json: FullDataJson): Pair<List<MedicationFullEntity>, List<DosageRegimenFullEntity>> {
        val medications = json.medications.map { medication ->
            MedicationFullEntity(
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
                    DosageRegimenFullEntity(
                        medicationId = medication.id,
                        route = regimen.route,
                        fromMonths = rule.fromMonths,
                        toMonths = rule.toMonths,
                        fromKg = rule.fromKg,
                        toKg = rule.toKg,
                        doseMin = rule.doseMin,
                        doseMax = rule.doseMax,
                        unit = rule.unit,
                        maxDoseMg = rule.maxDoseMg,
                        note = rule.note
                    )
                }
            }
        }

        return Pair(medications, regimens)
    }
}
