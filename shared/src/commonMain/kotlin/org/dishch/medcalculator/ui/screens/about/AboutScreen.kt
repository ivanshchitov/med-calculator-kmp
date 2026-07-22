package org.dishch.medcalculator.ui.screens.about

import MedCalculator.shared.AppConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.*
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.about_app
import medcalculator.shared.generated.resources.app_name
import medcalculator.shared.generated.resources.ic_syringe
import org.dishch.medcalculator.ui.components.cards.CalculationWarningCard
import org.dishch.medcalculator.ui.components.cards.ResultCard
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.about_app),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppDimens.SpacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppDimens.SpacingMediumSmall)
        ) {
            // App Icon
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(Res.drawable.ic_app),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                        .clip(RoundedCornerShape(AppDimens.CornerMediumSmall)),
                )
            }

            // App Name and Subtitle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.TextPrimary
                )
                Text(
                    text = stringResource(Res.string.app_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextSecondary
                )
                Surface(
                    modifier = Modifier.padding(AppDimens.SpacingExtraSmall),
                    shape = RoundedCornerShape(AppDimens.CornerSmall),
                    color = AppColors.InfoContainer) {

                    Box(
                        modifier = Modifier.padding(horizontal = AppDimens.SpacingSmall, vertical = AppDimens.SpacingExtraSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(
                                Res.string.version_format,
                                AppConfig.VERSION_NAME,
                                AppConfig.VERSION_CODE
                            ),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = AppColors.Primary
                        )
                    }
                }
            }

            // Description Card
            AboutInfoCard(
                icon = Icons.Outlined.Info,
                title = stringResource(Res.string.about_description_title),
                content = {
                    Text(
                        modifier = Modifier.padding(horizontal = AppDimens.SpacingMediumSmall),
                        text = stringResource(Res.string.about_description_text),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.TextPrimary
                    )
                }
            )

            // Authors Card
//            AboutInfoCard(
//                icon = Icons.Default.People,
//                title = stringResource(Res.string.authors_title),
//                content = {
//                    AuthorItem()
//                    Spacer(modifier = Modifier.height(AppDimens.SpacingSmall))
//                    AuthorItem()
//                }
//            )

            // Data Sources Card
            AboutInfoCard(
                icon = Icons.Default.Storage,
                title = stringResource(Res.string.data_sources_title),
                content = {
                    DataSourceItem(
                        name = stringResource(Res.string.source_ohlp),
                        url = "https://lk.regmed.ru/Register/EAEU_SmPC",
                        onClick = { uriHandler.openUri("https://lk.regmed.ru/Register/EAEU_SmPC") }
                    )
                    HorizontalDivider(color = AppColors.Border.copy(alpha = 0.5f))
                    DataSourceItem(
                        name = stringResource(Res.string.source_geotar),
                        url = "https://www.lsgeotar.ru/",
                        onClick = { uriHandler.openUri("https://www.lsgeotar.ru/") }
                    )
                    HorizontalDivider(color = AppColors.Border.copy(alpha = 0.5f))
                    DataSourceItem(
                        name = stringResource(Res.string.source_grls),
                        url = "https://grls.rosminzdrav.ru",
                        onClick = { uriHandler.openUri("https://grls.rosminzdrav.ru") }
                    )
                }
            )

            CalculationWarningCard()
        }
    }
}

@Composable
private fun AboutInfoCard(
    icon: ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ResultCard {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = AppDimens.SpacingMediumSmall)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.SpacingMediumSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(AppDimens.ResultIconContainerSize),
                    shape = RoundedCornerShape(AppDimens.CornerSmall),
                    color = AppColors.InfoContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = AppColors.Primary,
                            modifier = Modifier.size(AppDimens.IconSize)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(AppDimens.SpacingMediumSmall))
            content()
        }
    }
}

@Composable
private fun AuthorItem(name: String, role: String) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AppColors.TextPrimary
        )
        Text(
            text = role,
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.TextSecondary
        )
    }
}

@Composable
private fun DataSourceItem(name: String, url: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(horizontal = AppDimens.SpacingMediumSmall, vertical = AppDimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AppColors.TextPrimary
            )
            Text(
                text = url,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.Primary
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            tint = AppColors.Primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    MedCalculatorAppTheme {
        AboutScreen {  }
    }
}
