package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens

@Composable
fun AppCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    val cardShape = RoundedCornerShape(AppDimens.CardCorner)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .conditional(onClick != null) {
                clickable { onClick?.invoke() }
            },
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface,
            disabledContainerColor = AppColors.Surface
        ),
        border = BorderStroke(AppDimens.CardBorderWidth, AppColors.Border),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(AppDimens.CardPadding)
        ) {
            Row(
                verticalAlignment = verticalAlignment,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon container
                Surface(
                    modifier = Modifier.size(AppDimens.IconContainerSize),
                    shape = cardShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.IconLargeSize)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(AppDimens.CardSpacing))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.TextPrimary
                    )
                    content()
                }

                trailingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = AppColors.TextSecondary,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

/**
 * Extension to make a modifier conditional.
 * If the condition is true, the modifier will be applied, otherwise it won't.
 * This is useful to add (or not) the onClickListener to a card.
 */
fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
