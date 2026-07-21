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
import medcalculator.shared.generated.resources.age_range_format
import medcalculator.shared.generated.resources.age_supporting_months
import medcalculator.shared.generated.resources.age_supporting_years
import medcalculator.shared.generated.resources.contraindicated
import medcalculator.shared.generated.resources.mg_format
import medcalculator.shared.generated.resources.mg_per_kg_format
import medcalculator.shared.generated.resources.months_format
import medcalculator.shared.generated.resources.months_suffix
import medcalculator.shared.generated.resources.weight_range_format
import medcalculator.shared.generated.resources.years_format
import medcalculator.shared.generated.resources.years_suffix
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.DosageUnit
import org.dishch.medcalculator.domain.model.isWeightRangeValid
import org.dishch.medcalculator.domain.usecase.ValidationError
import org.dishch.medcalculator.domain.usecase.ValidationMessage
import org.dishch.medcalculator.formatAsDecimal
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

const val ONE_YEAR_MONTHS = 12
const val ADULT_AGE_MONTHS = 18 * ONE_YEAR_MONTHS

// Age formatting helpers

fun Int.isYears(): Boolean = this >= ONE_YEAR_MONTHS
fun Int.toAge(): Int = if (isYears()) this / ONE_YEAR_MONTHS else this

@Composable
fun Int.toFormattedAgeLimit(): String {
    return if (isYears() || this == 0) {
        val years = this / ONE_YEAR_MONTHS
        pluralStringResource(Res.plurals.age_limit_years_plurals, years, years)
    } else {
        pluralStringResource(Res.plurals.age_limit_months_plurals, this, this)
    }
}

@Composable
fun Int.toAgePluralsString(): String {
    val pluralsRes = if (isYears()) Res.plurals.years_format else Res.plurals.months_format
    return pluralStringResource(pluralsRes, this.toAge(), this.toAge())
}

// DosageRegimen extensions

val DosageRegimen.formattedMinDose: String
    get() = minDose?.formatAsDecimal() ?: ""

val DosageRegimen.formattedMaxDose: String
    get() = maxDose?.formatAsDecimal() ?: ""

val DosageRegimen.formattedDoseRange: String
    get() = if (minDose == maxDose)
        formattedMinDose
    else
        "$formattedMinDose-$formattedMaxDose"

val DosageRegimen.formattedAgeRange: String
    @Composable
    get() = when {
        toMonths >= ADULT_AGE_MONTHS -> fromMonths.toFormattedAgeLimit()
        else -> {
            var startAgeString = fromMonths.toAge().toString()
            if (fromMonths.isYears() != toMonths.isYears()) {
                startAgeString = fromMonths.toAgePluralsString()
            }
            stringResource(Res.string.age_range_format,
                startAgeString,
                toMonths.toAgePluralsString()
            )
        }
    }

val DosageRegimen.formattedWeightRange: String
    @Composable
    get() = if (isWeightRangeValid()) {
        stringResource(
            Res.string.weight_range_format,
            fromKg?.formatAsDecimal() ?: "",
            toKg?.formatAsDecimal() ?: ""
        )
    } else ""

val DosageRegimen.unitStringFormat: StringResource
    get() = if (dosageUnit == DosageUnit.MG_PER_KG)
        Res.string.mg_per_kg_format
    else
        Res.string.mg_format

val DosageRegimen.doseDisplayString: String
    @Composable
    get() =  if (minDose == null || maxDose == null)
        stringResource(Res.string.contraindicated)
    else
        stringResource(unitStringFormat, formattedDoseRange)

val DosageRegimen.formattedMaxSingleDose: String
    @Composable
    get() = stringResource(Res.string.mg_format, maxDoseMg?.formatAsDecimal() ?: "")

val DosageRegimen.icon: ImageVector
    get() = when {
        fromMonths < 12 -> Icons.Outlined.ChildFriendly   // less than 1 year
        fromMonths.toAge() < 8 -> Icons.Outlined.Face3    // less than 8 years
        fromMonths.toAge() < 14 -> Icons.Outlined.Face2   // less than 14
        else -> Icons.Outlined.Face6                      // 15 years and older
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

// ValidationError formatting

@Composable
fun ValidationMessage.asString(): String =
    when (this) {
        is ValidationMessage.Single -> stringResource(resource, arg)
        is ValidationMessage.Plural -> pluralStringResource(resource, quantity, arg)
}

@Composable
fun ValidationError.toMessage(): String = validationMessage.asString()
