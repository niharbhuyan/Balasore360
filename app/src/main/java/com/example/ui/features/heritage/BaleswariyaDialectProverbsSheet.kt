package com.example.ui.features.heritage

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyUpdateEngine

data class DialectWord(
    val baleswariyaTerm: String,
    val standardOdia: String,
    val englishMeaning: String,
    val usageExample: String
)

/**
 * Baleswariya Odia Dialect & Daily Auto-Updating Folk Proverbs Lexicon Sheet.
 */
@Composable
fun BaleswariyaDialectProverbsSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyPulse = remember { DailyUpdateEngine.getDailyPulse() }
    val todayProverb = dailyPulse.proverbOfTheDay

    var isPlayingAudio by remember { mutableStateOf(false) }

    val dialectWords = listOf(
        DialectWord(
            baleswariyaTerm = "କାହିଁକିରି (Kahinkiri)",
            standardOdia = "କାହିଁକି କିରେ",
            englishMeaning = "Why / For what reason? (Characteristic Northern Odia tag)",
            usageExample = "କାହିଁକିରି ସେଠିକି ଯିବୁନି?"
        ),
        DialectWord(
            baleswariyaTerm = "ଢୋଙ୍ଗା (Dhonga)",
            standardOdia = "ଛୋଟ ଡଙ୍ଗା / ନାଆ",
            englishMeaning = "Narrow country boat used in tidal estuaries",
            usageExample = "ଢୋଙ୍ଗାରେ ବସି ନଈ ପାରି ହେବା।"
        ),
        DialectWord(
            baleswariyaTerm = "ବାଡ଼ିସି (Badisi)",
            standardOdia = "ବନସୀ / ମାଛ ଧରା କଣ୍ଟା",
            englishMeaning = "Fishing hook & line used along coastal inlets",
            usageExample = "ଆଜି ବାଡ଼ିସିରେ ଭେକଟି ମାଛ ପଡ଼ିଛି।"
        ),
        DialectWord(
            baleswariyaTerm = "ବରଜ (Baraja)",
            standardOdia = "ପାନ କିଆରୀ କୁଟୀର",
            englishMeaning = "Thatched microclimate bamboo tunnel for growing betel vines",
            usageExample = "ସକାଳୁ ପାନ ବରଜକୁ ଯାଇ ପତ୍ର ତୋଳିବୁ।"
        ),
        DialectWord(
            baleswariyaTerm = "ଗିରସ୍ତ (Girasta)",
            standardOdia = "ଗୃହସ୍ଥ / ସମ୍ଭ୍ରାନ୍ତ ଲୋକ",
            englishMeaning = "Head of the rural household or respected master",
            usageExample = "ଗିରସ୍ତ ଘରକୁ ଆସିଲେଣି, ଜଳଖିଆ ବାଢ଼।"
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("baleswariya_dialect_proverbs_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🗣️", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Baleswariya Dialect & Proverbs",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ବାଲେଶ୍ୱରୀୟ କଥୋପକଥନ ଓ ଢଗଢମାଳି",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF6D28D9),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEDE9FE)
            ) {
                Text(
                    text = "Daily Auto-Proverb",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5B21B6)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // HERO CARD: Daily Auto-Updating Fakir Mohan Proverb of the Day
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
            border = BorderStroke(1.5.dp, Color(0xFFDDD6FE))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TODAY'S BALASORE PROVERB (ଆଜିର ଢଗ)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF6D28D9)
                        )
                    }

                    Text(
                        text = dailyPulse.formattedDate,
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Proverb Odia Text
                Text(
                    text = "“${todayProverb.odiaText}”",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4C1D95),
                        lineHeight = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // English Translation
                Text(
                    text = todayProverb.englishTranslation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF1E293B),
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Source & Context
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE9D5FF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "✍️ Source: ${todayProverb.sourceAuthor}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF6D28D9)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📖 Context: ${todayProverb.culturalContext}",
                            fontSize = 10.sp,
                            color = Color(0xFF475569),
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 Moral: ${todayProverb.moralTakeaway}",
                            fontSize = 10.sp,
                            color = Color(0xFF047857),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Pronunciation Audio Playback Simulator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isPlayingAudio) "🔊 Reciting proverb in traditional Odia..." else "Listen to authentic Baleswariya pronunciation",
                        fontSize = 11.sp,
                        color = if (isPlayingAudio) Color(0xFF8B5CF6) else Color(0xFF64748B),
                        fontWeight = if (isPlayingAudio) FontWeight.Bold else FontWeight.Normal
                    )

                    Button(
                        onClick = { isPlayingAudio = !isPlayingAudio },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlayingAudio) Color(0xFFDC2626) else Color(0xFF8B5CF6)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isPlayingAudio) "Stop" else "Listen", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Baleswariya Dialect Words Section
        Text(
            text = "Unique Northern Odia (Baleswariya) Dialect Words",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Balasore’s dialect carries a musical cadence influenced by border trade and ancient sea navigation.",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(8.dp))

        dialectWords.forEach { word ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = word.baleswariyaTerm,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF6D28D9)
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF3E8FF)
                        ) {
                            Text(
                                text = "Std Odia: ${word.standardOdia}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = Color(0xFF5B21B6),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(word.englishMeaning, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("🗣️ Example: “${word.usageExample}”", fontSize = 10.sp, color = Color(0xFF059669), fontStyle = FontStyle.Italic)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Baleswariya Lexicon", fontWeight = FontWeight.Bold)
        }
    }
}
