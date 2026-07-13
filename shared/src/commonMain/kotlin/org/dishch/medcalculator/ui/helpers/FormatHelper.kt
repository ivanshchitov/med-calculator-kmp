package org.dishch.medcalculator.ui.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChildFriendly
import androidx.compose.material.icons.outlined.Face2
import androidx.compose.material.icons.outlined.Face3
import androidx.compose.material.icons.outlined.Face6
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_limit_months_plurals
import medcalculator.shared.generated.resources.age_limit_years_plurals
import medcalculator.shared.generated.resources.age_months
import medcalculator.shared.generated.resources.age_range_format
import medcalculator.shared.generated.resources.age_supporting_months
import medcalculator.shared.generated.resources.age_supporting_years
import medcalculator.shared.generated.resources.age_years
import medcalculator.shared.generated.resources.mg_format
import medcalculator.shared.generated.resources.mg_per_kg_format
import medcalculator.shared.generated.resources.months_suffix
import medcalculator.shared.generated.resources.years_suffix
import org.dishch.medcalculator.domain.model.ADULT_AGE_MONTHS
import org.dishch.medcalculator.domain.model.Age
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.DosageUnit
import org.dishch.medcalculator.domain.model.toAge
import org.dishch.medcalculator.formatAsDecimal
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

// Age formatting helpers

val Age.formattedAgeLimit: String
    @Composable
    get() = if (isYears || quantity == 0) {
        pluralStringResource(Res.plurals.age_limit_years_plurals, quantity, quantity)
    } else {
        pluralStringResource(Res.plurals.age_limit_months_plurals, quantity, quantity)
    }

@Composable
fun Age.toPluralsString(): String =
    pluralStringResource(pluralRes, quantity, quantity)

private val Age.pluralRes: PluralStringResource
    get() = if (isYears) Res.plurals.age_years else Res.plurals.age_months

// DosageRegimen extensions

val DosageRegimen.fromAgeInYears: Int
    get() = fromMonths.toAge().quantity

val DosageRegimen.formattedMinDose: String
    get() = minDose?.formatAsDecimal() ?: ""

val DosageRegimen.formattedMaxDose: String
    get() = maxDose?.formatAsDecimal() ?: ""

val DosageRegimen.formattedDoseRange: String
    get() = "$formattedMinDose-$formattedMaxDose"

val DosageRegimen.formattedAgeRange: String
    @Composable
    get() = when {
        toMonths >= ADULT_AGE_MONTHS -> fromMonths.toAge().formattedAgeLimit
        else -> {
            var startAgeString = fromMonths.toAge().quantity.toString()
            if (fromMonths.toAge().isYears != toMonths.toAge().isYears) {
                startAgeString = fromMonths.toAge().toPluralsString()
            }
            stringResource(Res.string.age_range_format,
                startAgeString,
                toMonths.toAge().toPluralsString()
            )
        }
    }

val DosageRegimen.unitStringFormat: StringResource
    get() = if (dosageUnit == DosageUnit.MG_PER_KG)
        Res.string.mg_per_kg_format
    else
        Res.string.mg_format

val DosageRegimen.icon: ImageVector
    get() = when {
        fromAgeInYears < 1 -> Icons.Outlined.ChildFriendly // Infant
        fromAgeInYears < 12 -> Icons.Outlined.Face3        // Child
        fromAgeInYears < 18 -> Icons.Outlined.Face2        // Teen
        else -> Icons.Outlined.Face6                        // Senior
    }

// AgeUnit formatting helpers

val AgeUnit.suffix: PluralStringResource
    get() = when (this) {
        AgeUnit.MONTHS -> Res.plurals.months_suffix
        AgeUnit.YEARS -> Res.plurals.years_suffix
    }

val AgeUnit.supportingText: StringResource
    get() = when (this) {
        AgeUnit.MONTHS -> Res.string.age_supporting_months
        AgeUnit.YEARS -> Res.string.age_supporting_years
    }
