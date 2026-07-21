package org.dishch.medcalculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme

@Composable
fun ResultRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    unit: String? = null,
    iconColor: Color = AppColors.Primary,
    iconContainerColor: Color = AppColors.InfoContainer,
    valueColor: Color = AppColors.TextPrimary,
    supportingText: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppDimens.SpacingMediumSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(AppDimens.ResultIconContainerSize),
            shape = RoundedCornerShape(AppDimens.CornerSmall),
            color = iconContainerColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(AppDimens.IconSize)
                )
            }
        }

        Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.TextPrimary,
            modifier = Modifier.weight(1f),
            softWrap = true
        )

        Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))

        Column(horizontalAlignment = Alignment.End,
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            Row(verticalAlignment = Alignment.Bottom,
                modifier = Modifier.wrapContentWidth()) {
                Text(
                    modifier = Modifier.width(if (unit == null) IntrinsicSize.Min else IntrinsicSize.Max),
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = valueColor,
                    textAlign = TextAlign.End
                )
                if (unit != null) {
                    Spacer(modifier = Modifier.width(AppDimens.SpacingExtraSmall))
                    Text(
                        modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically),
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.TextSecondary,
                        textAlign = TextAlign.End
                    )
                }
            }
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )
            }
        }
    }
}

@Composable
@Preview
fun ResultRowPreview() {
    MedCalculatorAppTheme {
        ResultRow(
            icon = Icons.Filled.ArrowUpward,
            label = "Препарат",
            value = "Транексамовая кислота",
            unit = ""
        )
    }
}

@Composable
@Preview
fun ResultRowWithUnitPreview() {
    MedCalculatorAppTheme {
        ResultRow(
            icon = Icons.Filled.ArrowUpward,
            label = "Доза препарата",
            value = "100",
            unit = "мг."
        )
    }
}