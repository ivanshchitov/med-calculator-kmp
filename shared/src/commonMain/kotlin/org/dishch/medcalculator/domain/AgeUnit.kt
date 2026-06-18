package org.dishch.medcalculator.domain

import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.months
import medcalculator.shared.generated.resources.months_full
import medcalculator.shared.generated.resources.years
import medcalculator.shared.generated.resources.years_full
import org.jetbrains.compose.resources.StringResource

enum class AgeUnit(val suffix: StringResource, val label: StringResource) {
    MONTHS(Res.string.months, Res.string.months_full),
    YEARS(Res.string.years, Res.string.years_full)
}
