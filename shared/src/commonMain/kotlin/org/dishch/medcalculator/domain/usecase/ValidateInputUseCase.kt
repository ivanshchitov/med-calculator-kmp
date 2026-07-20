package org.dishch.medcalculator.domain.usecase

import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.min_months_supporting_text
import medcalculator.shared.generated.resources.min_weight_supporting_text
import medcalculator.shared.generated.resources.min_years_supporting_text
import medcalculator.shared.generated.resources.weight_supporting_text
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.formatAsDecimal
import org.dishch.medcalculator.ui.helpers.ONE_YEAR_MONTHS
import org.dishch.medcalculator.ui.helpers.isYears
import org.dishch.medcalculator.ui.helpers.supportingText
import org.dishch.medcalculator.ui.helpers.toAge
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import kotlin.text.isBlank

data class ValidationState(
    val isValid: Boolean,
    val errors: List<ValidationError> = emptyList()
)

sealed interface ValidationErrorCode {
    object InvalidWeightFormat : ValidationErrorCode
    object WeightTooLow : ValidationErrorCode
    object InvalidAgeFormat : ValidationErrorCode
    object MonthsTooLow : ValidationErrorCode
    object YearsTooLow : ValidationErrorCode
}

sealed interface ValidationMessage {
    data class Single(
        val resource: StringResource,
        val arg: Any
    ) : ValidationMessage

    data class Plural(
        val resource: PluralStringResource,
        val quantity: Int,
        val arg: Any
    ) : ValidationMessage
}

data class ValidationError(
    val code: ValidationErrorCode,
    val validationMessage: ValidationMessage,
)

class ValidateInputUseCase {

    operator fun invoke(
        weightString: String,
        ageString: String,
        ageUnit: AgeUnit,
        minMonths: Int,
        minWeight: Double?
    ): ValidationState {
        val errors = buildList {
            addAll(validateWeight(weightString, minWeight))
            addAll(validateAge(ageString, ageUnit, minMonths))
        }

        return ValidationState(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    private fun validateWeight(weightString: String, minWeight: Double?): List<ValidationError> {
        val weight = weightString.toDoubleOrNull() ?: 0.0
        val isWeightLessThanMin = minWeight != null && weight < minWeight
        val isWeightInputIncorrect = weightString.isBlank() || weight == 0.0 || weight > 150.0
        if (isWeightLessThanMin) {
            return listOf(ValidationError(
                ValidationErrorCode.WeightTooLow,
                ValidationMessage.Single(
                    Res.string.min_weight_supporting_text,
                    minWeight.formatAsDecimal()
                )
            ))
        }
        if (isWeightInputIncorrect) {
            return listOf(ValidationError(
                ValidationErrorCode.InvalidWeightFormat,
                ValidationMessage.Single(
                    Res.string.weight_supporting_text,
                    (minWeight ?: 1.0).formatAsDecimal()
                )
            ))
        }
        return emptyList()
    }

    private fun validateAge(ageString: String, ageUnit: AgeUnit, minMonths: Int): List<ValidationError> {
        val age = ageString.toIntOrNull() ?: 0
        val ageInMonths = if (ageUnit == AgeUnit.YEARS) age * ONE_YEAR_MONTHS else age
        val maxAge = if (ageUnit == AgeUnit.YEARS) 17 else 11
        val isAgeLessThanMin = (ageString.isBlank() && minMonths > 0) || ageInMonths < minMonths
        val isAgeInputIncorrect = (ageString.isBlank() && minMonths == 0) || age == 0 || age > maxAge

        if (isAgeLessThanMin) {
            return if (minMonths.isYears()) {
                listOf(ValidationError(
                ValidationErrorCode.YearsTooLow,
                    ValidationMessage.Plural(
                        Res.plurals.min_years_supporting_text,
                        minMonths.toAge().coerceAtLeast(1),
                        minMonths.toAge().coerceAtLeast(1)
                    )
                ))
            } else {
                listOf(ValidationError(
                    ValidationErrorCode.MonthsTooLow,
                    ValidationMessage.Single(
                        Res.string.min_months_supporting_text,
                        minMonths.toAge().coerceAtLeast(1)
                    )
                ))
            }
        }


        if (isAgeInputIncorrect) {
            return listOf(ValidationError(
                ValidationErrorCode.InvalidAgeFormat,
                ValidationMessage.Single(
                    ageUnit.supportingText,
                    if (ageUnit == AgeUnit.YEARS && !minMonths.isYears()) 1 else minMonths.toAge().coerceAtLeast(1)
                )
            ))
        }

        return emptyList()
    }
}
