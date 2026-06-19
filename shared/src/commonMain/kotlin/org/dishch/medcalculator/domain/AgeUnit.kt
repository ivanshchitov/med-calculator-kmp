package org.dishch.medcalculator.domain

import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_supporting_months
import medcalculator.shared.generated.resources.age_supporting_years
import medcalculator.shared.generated.resources.months_suffix
import medcalculator.shared.generated.resources.years_suffix
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

enum class AgeUnit(val suffix: PluralStringResource, val supportingText: StringResource) {
    MONTHS(Res.plurals.months_suffix, Res.string.age_supporting_months),
    YEARS(Res.plurals.years_suffix, Res.string.age_supporting_years)
}
