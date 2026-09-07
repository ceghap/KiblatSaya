package com.kiblatsaya.app.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiblatsaya.app.core.config.AppConfig
import com.kiblatsaya.app.core.designsystem.theme.HeadlineLargeSerif
import com.kiblatsaya.app.core.designsystem.theme.LabelSmallCaps
import com.kiblatsaya.app.core.designsystem.theme.LocalSanctuaryColors
import kiblatsaya.composeapp.generated.resources.Res
import kiblatsaya.composeapp.generated.resources.app_logo
import kiblatsaya.composeapp.generated.resources.symbol_white
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sanctuaryColors = LocalSanctuaryColors.current
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.92f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(450, easing = FastOutSlowInEasing))
        scaleAnim.animateTo(1f, animationSpec = tween(450, easing = FastOutSlowInEasing))
        delay(1200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(sanctuaryColors.surfaceBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSplashFinished()
            }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(Res.drawable.app_logo),
                contentDescription = "KiblatSaya Logo",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(22.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App Title
            Text(
                text = AppConfig.APP_NAME,
                style = HeadlineLargeSerif.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = sanctuaryColors.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = AppConfig.SUBTITLE.uppercase(),
                style = LabelSmallCaps.copy(
                    color = sanctuaryColors.primaryLight,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Company Attribution
            Text(
                text = "DIBANGUNKAN OLEH",
                style = LabelSmallCaps.copy(
                    color = sanctuaryColors.textTertiary,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Ashraf Systems Brand
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.symbol_white),
                    contentDescription = "Ashraf Systems Logo",
                    modifier = Modifier.size(44.dp),
                    colorFilter = ColorFilter.tint(sanctuaryColors.textPrimary)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "ASHRAF",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 2.sp,
                            color = sanctuaryColors.textPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "SYSTEMS",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            letterSpacing = 4.sp,
                            color = sanctuaryColors.primaryLight
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Version & Copyright
            Text(
                text = "${AppConfig.VERSION_DISPLAY} • ${AppConfig.COPYRIGHT}",
                style = LabelSmallCaps.copy(
                    color = sanctuaryColors.textTertiary.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}
