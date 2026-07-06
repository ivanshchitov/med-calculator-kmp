package org.dishch.medcalculator.domain.usecase

import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_supporting_months
import medcalculator.shared.generated.resources.age_supporting_years
import medcalculator.shared.generated.resources.weight_supporting_text
import org.dishch.medcalculator.domain.model.AgeUnit
import org.jetbrains.compose.resources.StringResource

data class ErrorMessages(
    val weightSupportingText: StringResource? = null,
    val ageSupportingText: StringResource? = null
)

class ValidationErrorMessagesUseCase {

    operator fun invoke(
        validationState: ValidationState,
        ageUnit: AgeUnit
    ): ErrorMessages {
        val weightError = if (!validationState.isValid && validationState.weight == null) {
            Res.string.weight_supporting_text
        } else {
            null
        }

        val ageError = if (!validationState.isValid && validationState.age == null) {
            if (ageUnit == AgeUnit.MONTHS) {
                Res.string.age_supporting_months
            } else {
                Res.string.age_supporting_years
            }
        } else {
            null
        }

        return ErrorMessages(
            weightSupportingText = weightError,
            ageSupportingText = ageError
        )
    }
}
