package org.dishch.medcalculator.domain

import kotlinx.serialization.Serializable
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_supporting_months
import medcalculator.shared.generated.resources.age_supporting_years
import medcalculator.shared.generated.resources.months_suffix
import medcalculator.shared.generated.resources.years_suffix
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class AgeUnit {
    MONTHS, YEARS;

    val suffix: PluralStringResource
        get() = when (this) {
            MONTHS -> Res.plurals.months_suffix
            YEARS -> Res.plurals.years_suffix
        }

    val supportingText: StringResource
        get() = when (this) {
            MONTHS -> Res.string.age_supporting_months
            YEARS -> Res.string.age_supporting_years
        }
}
