package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.calculation_warning
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun CalculationWarningCard(modifier: Modifier = Modifier) {
    ResultCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.SpacingMediumSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(AppDimens.ResultIconContainerSize),
                shape = RoundedCornerShape(AppDimens.CornerSmall),
                color = AppColors.InfoContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.MedicalInformation,
                        contentDescription = null,
                        tint = AppColors.Primary,
                        modifier = Modifier.size(AppDimens.IconSize)
                    )
                }
            }

            Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))

            Text(
                text = stringResource(Res.string.calculation_warning),
                color = AppColors.TextPrimary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
