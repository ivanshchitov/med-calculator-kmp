package org.dishch.medcalculator.domain

import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_supporting_months
import medcalculator.shared.generated.resources.age_supporting_years
import medcalculator.shared.generated.resources.months
import medcalculator.shared.generated.resources.years
import org.jetbrains.compose.resources.StringResource

enum class AgeUnit(val suffix: StringResource, val supportingText: StringResource) {
    MONTHS(Res.string.months, Res.string.age_supporting_months),
    YEARS(Res.string.years, Res.string.age_supporting_years)
}
