package org.dishch.medcalculator.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    suffix: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String = "",
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxIntegerDigits: Int? = null,
    maxFractionDigits: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            val processedValue = processInput(
                newValue = newValue,
                maxIntegerDigits = maxIntegerDigits,
                maxFractionDigits = maxFractionDigits,
            )
            onValueChange(processedValue)
        },
        textStyle = MaterialTheme.typography.titleLarge.copy(
            fontSize = AppDimens.InputTextSize,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                suffix = {
                    Text(
                        text = suffix,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isError) MaterialTheme.colorScheme.error else AppColors.TextSecondary
                    )
                },
                supportingText = {
                    Text(text = supportingText)
                },
                isError = isError,
                interactionSource = interactionSource,
                // Setup content paddings
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 4.dp // padding to the underline
                ),
                // Setup standard underline like in Material3
                container = {
                    TextFieldDefaults.ContainerBox(
                        enabled = true,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = AppColors.Border,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            )
        }
    )

}

private fun processInput(
    newValue: String,
    maxIntegerDigits: Int?,
    maxFractionDigits: Int?
): String {
    if (newValue.isEmpty()) return ""

    val allowDecimal = (maxFractionDigits ?: 0) > 0

    // Keep only digits and decimal point
    var value = buildString {
        newValue.forEach {
            when {
                it.isDigit() -> append(it)
                allowDecimal && (it == '.' || it == ',') -> append('.')
            }
        }
    }

    if (!allowDecimal) {
        value = value.filter(Char::isDigit)
        return maxIntegerDigits?.let { value.take(it) } ?: value
    }

    // Keep only the first dot
    val firstDot = value.indexOf('.')
    if (firstDot >= 0) {
        value = buildString {
            append(value.substring(0, firstDot + 1))
            append(value.substring(firstDot + 1).replace(".", ""))
        }
    }

    val parts = value.split('.', limit = 2)
    var integerPart = parts.getOrElse(0) { "" }
    var decimalPart = parts.getOrElse(1) { "" }

    // Restricting the number of characters before the decimal point
    maxIntegerDigits?.let {
        integerPart = integerPart.take(it)
    }

    // Resstrict the number of decimal digits
    maxFractionDigits?.let {
        decimalPart = decimalPart.take(it)
    }

    return when {
        firstDot == -1 -> integerPart
        decimalPart.isEmpty() -> "$integerPart."
        else -> "$integerPart.$decimalPart"
    }
}
