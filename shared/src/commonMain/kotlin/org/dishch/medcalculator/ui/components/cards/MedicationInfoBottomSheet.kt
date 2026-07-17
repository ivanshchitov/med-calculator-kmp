package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.model.formattedDosage
import org.dishch.medcalculator.ui.theme.AppColors
import org.jetbrains.compose.resources.stringResource
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationInfoBottomSheet(
    medication: Medication,
    regimens: List<DosageRegimen>,
    avatarColor: Color,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(AppDimens.SpacingMedium)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            MedicationHeader(medication, avatarColor)

            Spacer(modifier = Modifier.height(AppDimens.SpacingMedium))

            MedicationFullInfo(medication, regimens)
        }
    }
}

@Composable
private fun MedicationHeader(medication: Medication, avatarColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Avatar
        Box(
            modifier = Modifier
                .size(AppDimens.IconContainerSize)
                .background(avatarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = medication.name.firstOrNull()?.uppercase() ?: "",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(AppDimens.SpacingMedium))
        Column {
            Text(
                medication.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(Res.string.dosage_label, medication.formattedDosage),
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSecondary
            )
        }
    }
}

