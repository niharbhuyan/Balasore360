package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoAmberBg
import com.example.ui.theme.BentoAmberText
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900

/**
 * Empty state mode categorization.
 */
enum class FriendlyEmptyStateType {
    NEWS_NOT_LOADED,
    ALERTS_ALL_CLEAR,
    OFFLINE_NO_INTERNET,
    OFFLINE_ROOM_CACHED,
    WEATHER_LOADING
}

/**
 * Animated, artistically crafted Canvas illustrations for empty and offline states.
 */
@Composable
fun FriendlyEmptyStateIllustration(
    type: FriendlyEmptyStateType,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "illustration_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_anim"
    )
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_anim"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(140.dp)
    ) {
        when (type) {
            FriendlyEmptyStateType.NEWS_NOT_LOADED -> {
                // Newspaper and gentle coastal tea mug illustration
                Canvas(modifier = Modifier.size(130.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f + floatOffset

                    // Soft background halo
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(BentoBlueLight, BentoBlueLight.copy(alpha = 0f)),
                            center = Offset(cx, cy),
                            radius = size.width * 0.48f * pulse
                        ),
                        center = Offset(cx, cy),
                        radius = size.width * 0.48f * pulse
                    )

                    // Folded Newspaper back shadow
                    drawRoundRect(
                        color = BentoBorder,
                        topLeft = Offset(cx - 36f, cy - 30f),
                        size = Size(76f, 66f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // Folded Newspaper main body
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(cx - 40f, cy - 34f),
                        size = Size(74f, 64f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawRoundRect(
                        color = BentoPrimaryBlue.copy(alpha = 0.3f),
                        topLeft = Offset(cx - 40f, cy - 34f),
                        size = Size(74f, 64f),
                        cornerRadius = CornerRadius(8f, 8f),
                        style = Stroke(width = 3f)
                    )

                    // Newspaper Masthead & header
                    drawRoundRect(
                        color = BentoPrimaryBlue,
                        topLeft = Offset(cx - 34f, cy - 28f),
                        size = Size(36f, 8f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawCircle(
                        color = BentoAmberText,
                        radius = 4f,
                        center = Offset(cx + 20f, cy - 24f)
                    )

                    // Newspaper text placeholder lines
                    val lineYOffsets = listOf(-14f, -6f, 2f, 10f, 18f)
                    lineYOffsets.forEachIndexed { idx, yOff ->
                        val lineWidth = if (idx % 2 == 0) 58f else 46f
                        drawLine(
                            color = if (idx == 0) BentoSlate700 else BentoSlate400.copy(alpha = 0.7f),
                            start = Offset(cx - 34f, cy + yOff),
                            end = Offset(cx - 34f + lineWidth, cy + yOff),
                            strokeWidth = if (idx == 0) 3.5f else 2.5f,
                            cap = StrokeCap.Round
                        )
                    }

                    // Steaming morning tea cup badge
                    drawCircle(
                        color = BentoAmberBg,
                        radius = 18f,
                        center = Offset(cx + 34f, cy + 22f)
                    )
                    drawCircle(
                        color = BentoAmberText.copy(alpha = 0.5f),
                        radius = 18f,
                        center = Offset(cx + 34f, cy + 22f),
                        style = Stroke(width = 2.5f)
                    )
                    // Steam curl
                    val steamPath = Path().apply {
                        moveTo(cx + 34f, cy + 12f)
                        cubicTo(cx + 38f, cy + 6f, cx + 30f, cy + 0f, cx + 35f, cy - 6f)
                    }
                    drawPath(
                        path = steamPath,
                        color = BentoAmberText,
                        style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                    )
                }
            }

            FriendlyEmptyStateType.ALERTS_ALL_CLEAR -> {
                // Peaceful coastal sun, calm sea ripples & shield of protection
                Canvas(modifier = Modifier.size(130.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f + floatOffset

                    // Soft emerald protective aura
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(BentoGreenBg, BentoGreenBg.copy(alpha = 0f)),
                            center = Offset(cx, cy),
                            radius = size.width * 0.48f * pulse
                        ),
                        center = Offset(cx, cy),
                        radius = size.width * 0.48f * pulse
                    )

                    // Radiant morning sun
                    drawCircle(
                        color = Color(0xFFFDE68A),
                        radius = 24f,
                        center = Offset(cx, cy - 10f)
                    )
                    drawCircle(
                        color = Color(0xFFF59E0B),
                        radius = 18f,
                        center = Offset(cx, cy - 10f)
                    )

                    // Sun rays
                    for (i in 0 until 8) {
                        val angle = (i * 45) * (Math.PI / 180.0)
                        val r1 = 28f
                        val r2 = 35f
                        val x1 = cx + (r1 * Math.cos(angle)).toFloat()
                        val y1 = (cy - 10f) + (r1 * Math.sin(angle)).toFloat()
                        val x2 = cx + (r2 * Math.cos(angle)).toFloat()
                        val y2 = (cy - 10f) + (r2 * Math.sin(angle)).toFloat()
                        drawLine(
                            color = Color(0xFFF59E0B),
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }

                    // Calm gentle sea waves
                    val waveY1 = cy + 24f
                    val wavePath1 = Path().apply {
                        moveTo(cx - 45f, waveY1)
                        cubicTo(cx - 25f, waveY1 - 5f, cx - 15f, waveY1 + 5f, cx, waveY1)
                        cubicTo(cx + 15f, waveY1 - 5f, cx + 25f, waveY1 + 5f, cx + 45f, waveY1)
                    }
                    drawPath(
                        path = wavePath1,
                        color = Color(0xFF38BDF8),
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                    )

                    val waveY2 = cy + 34f
                    val wavePath2 = Path().apply {
                        moveTo(cx - 35f, waveY2)
                        cubicTo(cx - 15f, waveY2 - 4f, cx - 5f, waveY2 + 4f, cx + 5f, waveY2)
                        cubicTo(cx + 18f, waveY2 - 4f, cx + 25f, waveY2 + 4f, cx + 35f, waveY2)
                    }
                    drawPath(
                        path = wavePath2,
                        color = Color(0xFF0284C7),
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )

                    // Checkmark shield badge
                    drawCircle(
                        color = BentoGreenText,
                        radius = 16f,
                        center = Offset(cx + 36f, cy - 20f)
                    )
                    // Checkmark
                    val checkPath = Path().apply {
                        moveTo(cx + 28f, cy - 20f)
                        lineTo(cx + 34f, cy - 14f)
                        lineTo(cx + 44f, cy - 25f)
                    }
                    drawPath(
                        path = checkPath,
                        color = Color.White,
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )
                }
            }

            FriendlyEmptyStateType.OFFLINE_NO_INTERNET, FriendlyEmptyStateType.OFFLINE_ROOM_CACHED -> {
                // Offline cloud with gentle signal & local storage vault
                Canvas(modifier = Modifier.size(130.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f + floatOffset

                    // Amber/neutral warning halo
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(BentoAmberBg, BentoAmberBg.copy(alpha = 0f)),
                            center = Offset(cx, cy),
                            radius = size.width * 0.48f * pulse
                        ),
                        center = Offset(cx, cy),
                        radius = size.width * 0.48f * pulse
                    )

                    // Cloud Body
                    val cloudPath = Path().apply {
                        moveTo(cx - 30f, cy + 10f)
                        cubicTo(cx - 45f, cy + 10f, cx - 48f, cy - 10f, cx - 35f, cy - 15f)
                        cubicTo(cx - 35f, cy - 35f, cx - 10f, cy - 40f, cx, cy - 28f)
                        cubicTo(cx + 10f, cy - 38f, cx + 38f, cy - 30f, cx + 35f, cy - 12f)
                        cubicTo(cx + 48f, cy - 8f, cx + 46f, cy + 10f, cx + 30f, cy + 10f)
                        close()
                    }
                    drawPath(
                        path = cloudPath,
                        color = Color.White
                    )
                    drawPath(
                        path = cloudPath,
                        color = BentoAmberText.copy(alpha = 0.5f),
                        style = Stroke(width = 3f)
                    )

                    // Cloud offline diagonal slash
                    drawLine(
                        color = BentoAmberText,
                        start = Offset(cx - 24f, cy - 32f),
                        end = Offset(cx + 24f, cy + 16f),
                        strokeWidth = 3.5f,
                        cap = StrokeCap.Round
                    )

                    // Local Room Database Vault badge
                    drawRoundRect(
                        color = BentoBlueLight,
                        topLeft = Offset(cx - 24f, cy + 16f),
                        size = Size(48f, 22f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = BentoPrimaryBlue,
                        topLeft = Offset(cx - 24f, cy + 16f),
                        size = Size(48f, 22f),
                        cornerRadius = CornerRadius(6f, 6f),
                        style = Stroke(width = 2.5f)
                    )

                    // Database cylinder lines inside badge
                    drawLine(
                        color = BentoPrimaryBlue,
                        start = Offset(cx - 16f, cy + 23f),
                        end = Offset(cx + 16f, cy + 23f),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = BentoPrimaryBlue,
                        start = Offset(cx - 16f, cy + 30f),
                        end = Offset(cx + 16f, cy + 30f),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }

            FriendlyEmptyStateType.WEATHER_LOADING -> {
                // Spinning cyclone/radar breeze & coastal weather symbols
                Canvas(modifier = Modifier.size(130.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(BentoBlueLight, BentoBlueLight.copy(alpha = 0f)),
                            center = Offset(cx, cy),
                            radius = size.width * 0.48f * pulse
                        ),
                        center = Offset(cx, cy),
                        radius = size.width * 0.48f * pulse
                    )

                    // Coastal breeze curves
                    val breeze1 = Path().apply {
                        moveTo(cx - 40f, cy - 12f)
                        cubicTo(cx - 10f, cy - 25f, cx + 15f, cy - 5f, cx + 35f, cy - 18f)
                    }
                    drawPath(
                        path = breeze1,
                        color = BentoPrimaryBlue,
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                    )

                    val breeze2 = Path().apply {
                        moveTo(cx - 30f, cy + 6f)
                        cubicTo(cx - 5f, cy - 6f, cx + 20f, cy + 14f, cx + 42f, cy + 2f)
                    }
                    drawPath(
                        path = breeze2,
                        color = Color(0xFF38BDF8),
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )

                    // Atmospheric drop
                    drawCircle(
                        color = Color(0xFF0284C7),
                        radius = 8f,
                        center = Offset(cx, cy + 24f)
                    )
                }
            }
        }
    }
}

/**
 * Friendly Empty State Card with Custom Illustrations.
 * Used for:
 * 1. News not yet loaded or empty category
 * 2. Active weather alerts "All Clear" or not yet loaded
 * 3. Offline without network connection (Room offline cache enabled)
 */
@Composable
fun FriendlyEmptyStateCard(
    type: FriendlyEmptyStateType,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    odiaTitle: String? = null,
    isActionLoading: Boolean = false,
    actionButtonText: String? = "Pull to Refresh",
    actionButtonIcon: ImageVector = Icons.Default.Refresh,
    onActionClick: (() -> Unit)? = null,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    tipsList: List<String> = emptyList()
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("friendly_empty_state_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Illustrated Canvas Icon
            FriendlyEmptyStateIllustration(type = type)

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp
                ),
                color = BentoSlate900,
                textAlign = TextAlign.Center
            )

            // Odia Sub-headline if provided
            if (!odiaTitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = odiaTitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.5.sp
                    ),
                    color = BentoPrimaryBlue,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle & helpful message
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                color = BentoSlate500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // Helpful tips pill list
            if (tipsList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BentoBlueLight.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tipsList.forEach { tip ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("✨", fontSize = 11.sp)
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = BentoSlate700
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            if (onActionClick != null || onSecondaryClick != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (onSecondaryClick != null && secondaryButtonText != null) {
                        OutlinedButton(
                            onClick = onSecondaryClick,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, BentoBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BentoSlate700),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("empty_state_secondary_button")
                        ) {
                            Text(
                                text = secondaryButtonText,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    if (onActionClick != null && actionButtonText != null) {
                        Button(
                            onClick = onActionClick,
                            enabled = !isActionLoading,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BentoPrimaryBlue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("empty_state_action_button")
                        ) {
                            if (isActionLoading) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Refreshing...")
                            } else {
                                Icon(
                                    imageVector = actionButtonIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = actionButtonText,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
