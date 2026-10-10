package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400

/**
 * Text-to-Speech Audio Player Component for News Articles.
 * Allows users to listen to regional news articles in either English or Odia (ଓଡ଼ିଆ),
 * with real-time waveform visualizers, speech rate toggling, and audio controls.
 */
@Composable
fun NewsAudioPlayerComponent(
    articleId: String,
    englishTitle: String,
    odiaTitle: String,
    englishContent: String,
    odiaContent: String,
    isSpeaking: Boolean,
    isPaused: Boolean,
    selectedLanguage: AppLanguage,
    speechSpeed: Float,
    statusMessage: String? = null,
    onPlay: (AppLanguage) -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("news_tts_player_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.5.dp, if (isSpeaking) BentoPrimaryBlue.copy(alpha = 0.6f) else BentoBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Header Bar: Audio Edition Label + Animated Sound Wave
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSpeaking) BentoPrimaryBlue else BentoSlate100,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.Headphones,
                                contentDescription = "Audio Edition",
                                tint = if (isSpeaking) Color.White else BentoPrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AUDIO EDITION • ଶ୍ରବଣ ସଂସ୍କରଣ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.6.sp
                            ),
                            color = BentoPrimaryBlue
                        )
                        Text(
                            text = if (selectedLanguage == AppLanguage.ODIA) {
                                if (isSpeaking) "ଓଡ଼ିଆରେ ପଢ଼ାଚାଲିଛି..." else "ଓଡ଼ିଆ ଖବର ଶୁଣନ୍ତୁ"
                            } else {
                                if (isSpeaking) "Reading in English..." else "Listen in English"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Waveform Equalizer when speaking
                if (isSpeaking) {
                    AudioEqualizerWaveform(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(end = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Playback Controls & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Play / Pause Button
                Button(
                    onClick = {
                        if (isSpeaking) {
                            onPause()
                        } else {
                            onPlay(selectedLanguage)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BentoPrimaryBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("news_tts_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isSpeaking) "Pause Audio" else "Play Audio",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isSpeaking -> "Pause"
                            isPaused -> "Resume"
                            selectedLanguage == AppLanguage.ODIA -> "ଶୁଣନ୍ତୁ (Play)"
                            else -> "Listen (Play)"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Stop Button
                if (isSpeaking || isPaused) {
                    OutlinedIconButton(
                        onClick = onStop,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BentoBorder),
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("news_tts_stop_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Audio",
                            tint = Color(0xFFEF4444)
                        )
                    }
                }

                // Speed Selector Pill
                FilledTonalButton(
                    onClick = {
                        val nextSpeed = when (speechSpeed) {
                            0.8f -> 1.0f
                            1.0f -> 1.25f
                            else -> 0.8f
                        }
                        onSpeedChange(nextSpeed)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("news_tts_speed_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Speech Speed",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${speechSpeed}x",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Language Selector Row: English | Odia (ଓଡ଼ିଆ)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = BentoSlate400,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Voice:",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BentoSlate400
                    )
                }

                // English Voice Chip
                FilterChip(
                    selected = selectedLanguage == AppLanguage.ENGLISH,
                    onClick = { onLanguageChange(AppLanguage.ENGLISH) },
                    label = {
                        Text(
                            text = "English",
                            fontWeight = if (selectedLanguage == AppLanguage.ENGLISH) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BentoBlueLight,
                        selectedLabelColor = BentoPrimaryBlue
                    ),
                    modifier = Modifier.testTag("news_tts_lang_english")
                )

                // Odia Voice Chip
                FilterChip(
                    selected = selectedLanguage == AppLanguage.ODIA,
                    onClick = { onLanguageChange(AppLanguage.ODIA) },
                    label = {
                        Text(
                            text = "ଓଡ଼ିଆ (Odia)",
                            fontWeight = if (selectedLanguage == AppLanguage.ODIA) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFEF3C7),
                        selectedLabelColor = Color(0xFFB45309)
                    ),
                    modifier = Modifier.testTag("news_tts_lang_odia")
                )
            }

            // Status notice banner if applicable
            if (!statusMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.5.sp,
                        color = BentoSlate400
                    )
                )
            }
        }
    }
}

/**
 * Animated sound wave equalizer visualizer with 5 bouncing bars.
 */
@Composable
fun AudioEqualizerWaveform(
    modifier: Modifier = Modifier,
    barColor: Color = BentoPrimaryBlue
) {
    val transition = rememberInfiniteTransition(label = "waveform_anim")

    val bar1 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )
    val bar2 by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(320, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )
    val bar3 by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )
    val bar4 by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(380, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar4"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val heights = listOf(bar1, bar2, bar3, bar4, bar2)
        heights.forEach { frac ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((22 * frac).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }
}
