package com.example.ui.features.heritage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.HeritageAudioStory
import com.example.data.repository.SmartFeaturesRepository
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import kotlinx.coroutines.delay

@Composable
fun RaibaniaAudioStoryWalkSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onGenerateLyriaTrack: (prompt: String, isShortClip: Boolean) -> Unit = { _, _ -> },
    isMusicGenerating: Boolean = false,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val stories = SmartFeaturesRepository.heritageStories
    var activeStoryId by remember { mutableStateOf<String?>(stories.firstOrNull()?.id) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableFloatStateOf(0f) }
    var customMusicPrompt by remember { mutableStateOf("") }
    var generatedMusicStatus by remember { mutableStateOf<String?>(null) }

    // Simulated audio playback progression
    LaunchedEffect(isPlaying, activeStoryId) {
        if (isPlaying) {
            while (playbackProgress < 1f) {
                delay(400)
                playbackProgress += 0.02f
            }
            isPlaying = false
            playbackProgress = 0f
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("raibania_audio_walk_sheet"),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "HERITAGE AUDIO & LYRIA MUSIC",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFA5B4FC),
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF818CF8),
                            modifier = Modifier.size(10.dp)
                        ) {}
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ରାଏବଣିଆ ଦୁର୍ଗ ଓ ଐତିହ୍ୟ ଅଡିଓ ୱାକ୍"
                        else
                            "Raibania & Heritage Audio Walk",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ରାଏବଣିଆର ବିରାଟ ଦୁର୍ଗ, ଭୂଷଣ୍ଢେଶ୍ୱର ବିଶାଳକାୟ ଶିବଲିଙ୍ଗ, ଇଞ୍ଚୁଡ଼ି ଲବଣ ସତ୍ୟାଗ୍ରହ ଏବଂ ଶାନ୍ତି କାନନର ଲାଇଭ୍ ଐତିହ୍ୟ କାହାଣୀ ଓ Lyria ଏଆଇ ସଂଗୀତ।"
                        else
                            "Immersive storytelling and Google Lyria AI soundscape generator for Balasore's ancient citadels, freedom memorials, and sacred stone colossi.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Active Audio Player Console
        item {
            val currentStory = stories.find { it.id == activeStoryId } ?: stories.first()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFC7D2FE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEEF2FF)
                        ) {
                            Text(
                                text = "NOW PLAYING NARRATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF4338CA)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = "${currentStory.durationSeconds}s duration",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) currentStory.titleOd else currentStory.titleEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${currentStory.monumentEn} • ${currentStory.era}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF4F46E5),
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Audio Progress Bar
                    LinearProgressIndicator(
                        progress = { playbackProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF4F46E5),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Speaker",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPlaying) "Playing Ambient Track" else "Paused",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4F46E5))
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Narration Script Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) currentStory.narrationScriptOd else currentStory.narrationScriptEn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Section: Lyria Music Generation Lab
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                border = BorderStroke(1.dp, Color(0xFFE9D5FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Music",
                            tint = Color(0xFF7E22CE),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Lyria AI Music Synthesizer",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF581C87)
                                )
                            )
                            Text(
                                text = "Models: lyria-3-clip-preview (30s) & lyria-3-pro-preview (full track)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    color = Color(0xFF6B21A8)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customMusicPrompt,
                        onValueChange = { customMusicPrompt = it },
                        placeholder = {
                            Text(
                                text = "e.g., Traditional Odissi temple flute with mardala rhythm at Remuna...",
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7E22CE),
                            unfocusedBorderColor = Color(0xFFD8B4FE)
                        ),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val prompt = customMusicPrompt.ifBlank { "Ancient war drums and meditative flutes for Raibania medieval citadel" }
                                onGenerateLyriaTrack(prompt, true)
                                generatedMusicStatus = "Generating 30s clip with lyria-3-clip-preview..."
                            },
                            enabled = !isMusicGenerating,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E22CE)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isMusicGenerating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("30s Clip (Lyria Clip)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                val prompt = customMusicPrompt.ifBlank { "Serene classical Odissi sitar and flute raga for Balasore heritage" }
                                onGenerateLyriaTrack(prompt, false)
                                generatedMusicStatus = "Generating full track with lyria-3-pro-preview..."
                            },
                            enabled = !isMusicGenerating,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF581C87)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Full Track (Lyria Pro)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (generatedMusicStatus != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = generatedMusicStatus ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.5.sp,
                                color = Color(0xFF7E22CE),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // Section: Heritage Story Index
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଅଡିଓ କାହାଣୀ ସୂଚୀ (Tap to Play)" else "Audio Monument Directory",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        items(stories, key = { it.id }) { story ->
            val isSelected = story.id == activeStoryId
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFEEF2FF) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) Color(0xFF6366F1) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                ),
                onClick = {
                    activeStoryId = story.id
                    playbackProgress = 0f
                    isPlaying = true
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) story.titleOd else story.titleEn,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "${story.monumentEn} • ${story.distanceKm} km",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isSelected && isPlaying) Color(0xFF4F46E5) else Color(0xFFE0E7FF),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isSelected && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = if (isSelected && isPlaying) Color.White else Color(0xFF4F46E5),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
