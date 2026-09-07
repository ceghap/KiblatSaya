package com.kiblatsaya.app.presentation.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiblatsaya.app.core.config.AppConfig
import com.kiblatsaya.app.core.designsystem.theme.BodyMedium
import com.kiblatsaya.app.core.designsystem.theme.BodySmall
import com.kiblatsaya.app.core.designsystem.theme.LabelSmallCaps
import com.kiblatsaya.app.core.designsystem.theme.LocalSanctuaryColors
import com.kiblatsaya.app.core.designsystem.theme.TitleMediumSerif
import com.kiblatsaya.app.domain.theme.SpiritualPalette
import com.kiblatsaya.app.domain.theme.ThemeDisplayMode
import kiblatsaya.composeapp.generated.resources.Res
import kiblatsaya.composeapp.generated.resources.symbol_white
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionBottomSheet(
    currentPalette: SpiritualPalette,
    currentDisplayMode: ThemeDisplayMode,
    onSelectPalette: (SpiritualPalette) -> Unit,
    onSelectDisplayMode: (ThemeDisplayMode) -> Unit,
    onDismiss: () -> Unit
) {
    val sanctuaryColors = LocalSanctuaryColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uriHandler = LocalUriHandler.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = sanctuaryColors.surfaceContainer,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tema & Rupa Bentuk",
                    style = TitleMediumSerif.copy(color = sanctuaryColors.textPrimary, fontSize = 20.sp)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = sanctuaryColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seksyen Palet Warna
            Text(
                text = "PALET WARNA",
                style = LabelSmallCaps.copy(color = sanctuaryColors.textTertiary, fontSize = 10.sp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpiritualPalette.entries.forEach { palette ->
                    val isSelected = palette == currentPalette
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) sanctuaryColors.primary.copy(alpha = 0.15f) else sanctuaryColors.surfaceElevated)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) sanctuaryColors.primaryLight else sanctuaryColors.borderSubtle,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectPalette(palette) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(palette.previewColor)
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = palette.titleMalay,
                                style = BodyMedium.copy(
                                    color = if (isSelected) sanctuaryColors.primaryLight else sanctuaryColors.textPrimary,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Dipilih",
                                tint = sanctuaryColors.primaryLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Seksyen Mod Paparan
            Text(
                text = "MOD PAPARAN",
                style = LabelSmallCaps.copy(color = sanctuaryColors.textTertiary, fontSize = 10.sp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeDisplayMode.entries.forEach { mode ->
                    val isSelected = mode == currentDisplayMode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) sanctuaryColors.primary.copy(alpha = 0.15f) else sanctuaryColors.surfaceElevated)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) sanctuaryColors.primaryLight else sanctuaryColors.borderSubtle,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectDisplayMode(mode) }
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (mode) {
                                ThemeDisplayMode.GELAP -> "Gelap"
                                ThemeDisplayMode.CERAH -> "Cerah"
                                ThemeDisplayMode.SISTEM -> "Sistem"
                            },
                            style = BodySmall.copy(
                                color = if (isSelected) sanctuaryColors.primaryLight else sanctuaryColors.textPrimary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Seksyen Aplikasi Berkaitan (Cross-Promotion)
            Text(
                text = "APLIKASI KAMI",
                style = LabelSmallCaps.copy(color = sanctuaryColors.textTertiary, fontSize = 10.sp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(sanctuaryColors.surfaceElevated)
                    .border(1.dp, sanctuaryColors.borderSubtle, RoundedCornerShape(14.dp))
                    .clickable {
                        try {
                            uriHandler.openUri(AppConfig.WIRIDLY_PLAY_STORE_URL)
                        } catch (_: Exception) {}
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(sanctuaryColors.primary.copy(alpha = 0.15f))
                        .border(1.dp, sanctuaryColors.primaryLight.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Wiridly",
                        tint = sanctuaryColors.primaryLight,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = AppConfig.WIRIDLY_TITLE,
                        style = BodyMedium.copy(
                            color = sanctuaryColors.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = AppConfig.WIRIDLY_SUBTITLE,
                        style = BodySmall.copy(
                            color = sanctuaryColors.textSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = "Buka di Play Store",
                    tint = sanctuaryColors.primaryLight,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Seksyen Hak Cipta & Jenama Syarikat
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(sanctuaryColors.borderSubtle)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.symbol_white),
                        contentDescription = "Ashraf Systems Logo",
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(sanctuaryColors.textTertiary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${AppConfig.APP_NAME} ${AppConfig.VERSION_DISPLAY}",
                        style = LabelSmallCaps.copy(
                            color = sanctuaryColors.textSecondary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${AppConfig.COPYRIGHT} • Hak Cipta Terpelihara",
                    style = BodySmall.copy(
                        color = sanctuaryColors.textTertiary,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
