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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay

data class HeritageStory(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val durationSeconds: Int,
    val englishNarrative: String,
    val odiaNarrative: String,
    val themeColor: Long
)

/**
 * Bilingual Heritage Audio Snippets (English & Odia) player component.
 */
@Composable
fun HeritageAudioSnippetsSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stories = remember {
        listOf(
            HeritageStory(
                id = "story_1",
                title = "Fakir Mohan Senapati & Odia Renaissance",
                odiaTitle = "ବ୍ୟାସକବି ଫକୀର ମୋହନ ଓ ଶାନ୍ତିକାନନ",
                durationSeconds = 118,
                englishNarrative = "Welcome to Shanti Kanan in Balasore, the tranquil grove where Vyasakabi Fakir Mohan Senapati founded modern Odia prose. Born in Mallikashpur, Balasore in 1843, he championed the Odia language during a critical era. Here in Balasore, he established the historic Utkal Press in 1868 and penned his masterpiece 'Chha Mana Atha Guntha' (Six Acres and a Third), establishing social realism in Indian literature. Walking through Shanti Kanan, you can feel the serenity beneath the ancestral trees where literary luminaries gathered.",
                odiaNarrative = "ବାଲେଶ୍ୱରର ଶାନ୍ତିକାନନକୁ ସ୍ୱାଗତ। ଏହା ହେଉଛି ସେହି ପବିତ୍ର ସ୍ଥାନ ଯେଉଁଠାରେ ବ୍ୟାସକବି ଫକୀର ମୋହନ ସେନାପତି ଆଧୁନିକ ଓଡ଼ିଆ ଗଦ୍ୟ ସାହିତ୍ୟର ମୂଳଦୁଆ ପକାଇଥିଲେ। ୧୮୪୩ ମସିହାରେ ବାଲେଶ୍ୱରର ମଲ୍ଲିକାଶପୁରରେ ଜନ୍ମଗ୍ରହଣ କରି ସେ ଓଡ଼ିଆ ଭାଷା ସୁରକ୍ଷା ଆନ୍ଦୋଳନର ନେତୃତ୍ୱ ନେଇଥିଲେ। ୧୮୬୮ରେ ତାଙ୍କ ପ୍ରତିଷ୍ଠିତ ଉତ୍କଳ ପ୍ରେସ୍ ଏବଂ କାଳଜୟୀ ଉପନ୍ୟାସ 'ଛ ମାଣ ଆଠ ଗୁଣ୍ଠ' ଭାରତୀୟ ସାହିତ୍ୟରେ ନୂଆ ଦିଗ୍‌ଦର୍ଶନ ଦେଇଥିଲା।",
                themeColor = 0xFF7C3AED
            ),
            HeritageStory(
                id = "story_2",
                title = "Dr. Kalam & Chandipur's Missile Trail",
                odiaTitle = "ଡ଼. କଲାମ ଓ ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଘାଟି",
                durationSeconds = 135,
                englishNarrative = "Chandipur is not just known for its vanishing sea; it is the cradle of India's strategic defense. In the 1980s and 90s, Dr. APJ Abdul Kalam walked these very sands while directing the Integrated Test Range (ITR). The vast shallow continental shelf made Chandipur the world's most ideal missile flight interceptor test corridor. Landmark missiles including Agni, Prithvi, and Akash leaped toward the Bay of Bengal from nearby launch complexes. Today, Dr. Kalam Island stands as an eternal tribute to Indian scientific ingenuity.",
                odiaNarrative = "ଚାନ୍ଦିପୁର କେବଳ ଲୁଚକାଳି ଖେଳୁଥିବା ସମୁଦ୍ର ପାଇଁ ପ୍ରସିଦ୍ଧ ନୁହେଁ, ଏହା ହେଉଛି ଭାରତର ସୁରକ୍ଷା ଓ ଗୌରବର କେନ୍ଦ୍ରସ୍ଥଳୀ। ୧୯୮୦-୯୦ ଦଶକରେ ଭାରତର ପୂର୍ବତନ ରାଷ୍ଟ୍ରପତି ତଥା ମିସାଇଲ ମ୍ୟାନ ଡ଼. ଏ.ପି.ଜେ. ଅବ୍ଦୁଲ କଲାମ ଏହି ବେଳାଭୂମିରେ ରହି ଅଗ୍ନି, ପୃଥ୍ବୀ ଓ ଆକାଶ ପରି କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷଣର ନେତୃତ୍ୱ ନେଇଥିଲେ। ଚାନ୍ଦିପୁରର ବିସ୍ତୃତ ସମୁଦ୍ର ଉପକୂଳ ଏହାକୁ ବିଶ୍ୱର ସର୍ବୋତ୍କୃଷ୍ଟ ପରୀକ୍ଷଣ କେନ୍ଦ୍ର ଭାବେ ଗଢ଼ିତୋଳିଛି।",
                themeColor = 0xFF0284C7
            ),
            HeritageStory(
                id = "story_3",
                title = "Emami Jagannath Temple & Black Pagoda Heritage",
                odiaTitle = "ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିରର ଭାସ୍କର୍ଯ୍ୟ",
                durationSeconds = 124,
                englishNarrative = "Rising majestically over 78 feet along Remuna road, the Emami Jagannath Temple reflects the supreme architectural grandeur of classical Kalinga style. Built with intricately hand-carved Baulamala stone from Khandolite quarries, the vimana and jagamohana reproduce the authentic proportions of Puri's Srimandir. Inside, the sanctum houses Lord Jagannath, Balabhadra, Subhadra, and Sudarshana Chakra, illuminated by sacred ghee deepams and framed by landscaped flower gardens and sacred lotus ponds.",
                odiaNarrative = "ରେମୁଣା ରୋଡ୍‌ରେ ପ୍ରତିଷ୍ଠିତ ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର କଳିଙ୍ଗ ସ୍ଥାପତ୍ୟ କଳାର ଏକ ଅନନ୍ୟ ନିଦର୍ଶନ। ୭୮ ଫୁଟ ଉଚ୍ଚତା ବିଶିଷ୍ଟ ଏହି ମନୋରମ ମନ୍ଦିର ଖଣ୍ଡୋଲାଇଟ୍ ପ୍ରସ୍ତର ଖୋଦେଇରେ ନିର୍ମିତ। ପୁରୀ ଶ୍ରୀମନ୍ଦିର ଢାଞ୍ଚାରେ ନିର୍ମିତ ଏହି ମନ୍ଦିରରେ ଚତୁର୍ଦ୍ଧାମୂର୍ତ୍ତିଙ୍କ ଦର୍ଶନ ସହିତ ଆଖପାଖର ସୁନ୍ଦର ଉଦ୍ୟାନ ଓ ପୁଷ୍କରିଣୀ ଶ୍ରଦ୍ଧାଳୁମାନଙ୍କୁ ଭକ୍ତିଭାବରେ ବିମୋହିତ କରେ।",
                themeColor = 0xFFD97706
            ),
            HeritageStory(
                id = "story_4",
                title = "Panchalingeswar Perennial Hill Spring",
                odiaTitle = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପ୍ରାକୃତିକ ଝରଣା ଓ ଶୈବ ପୀଠ",
                durationSeconds = 110,
                englishNarrative = "Perched on the verdant slopes of the Nilagiri mountain range, Panchalingeswar is a sanctuary where nature and spirituality unite. Here, five natural granite Shiva lingas lie perpetually submerged beneath a crystal-clear hill brook. Devotees climb 263 stone steps through sal and teak canopies to touch the sacred lingas beneath the flowing cold stream. Legend connects this holy spot to King Banasura, who worshipped Lord Shiva here in the Treta Yuga.",
                odiaNarrative = "ନୀଳଗିରି ପାହାଡ଼ର ସବୁଜ ବନାନୀ କୋଳରେ ଅବସ୍ଥିତ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଏକ ପ୍ରାକୃତିକ ତଥା ଆଧ୍ୟାତ୍ମିକ କ୍ଷେତ୍ର। ଏଠାରେ ପାହାଡ଼ିଆ ଝରଣାର ଶୀତଳ ଜଳଧାରା ମଧ୍ୟରେ ପାଞ୍ଚୋଟି ପ୍ରାକୃତିକ ଶିବଲିଙ୍ଗ ସଦାସର୍ବଦା ନିମଜ୍ଜିତ ରହିଥାନ୍ତି। ୨୬୩ ପାହାଚ ଚଢ଼ି ଭକ୍ତମାନେ ଶାଳ ବଣ ଦେଇ ଯାଇ ପ୍ରବାହିତ ଝରଣା ଭିତରେ ଶିବଲିଙ୍ଗ ସ୍ପର୍ଶ କରି ପୂଜାର୍ଚ୍ଚନା କରନ୍ତି।",
                themeColor = 0xFF059669
            )
        )
    }

    val context = LocalContext.current
    var selectedStoryIndex by remember { mutableIntStateOf(0) }
    val currentStory = stories[selectedStoryIndex]

    var isOdiaSelected by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackProgressSeconds by remember { mutableFloatStateOf(0f) }

    // Audio Playback ticker
    LaunchedEffect(isPlaying, selectedStoryIndex) {
        while (isPlaying) {
            delay(1000L)
            if (playbackProgressSeconds < currentStory.durationSeconds) {
                playbackProgressSeconds += 1f
            } else {
                isPlaying = false
                playbackProgressSeconds = 0f
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("heritage_audio_snippets_sheet")
    ) {
        // Top Header
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
                                listOf(Color(0xFF7C3AED), Color(0xFF6D28D9))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Heritage Audio Stories",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ବାଲେଶ୍ୱର ଐତିହ୍ୟ ଅଡିଓ କାହାଣୀ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF7C3AED),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // Odia vs English switcher
            Surface(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFDDD6FE)),
                color = Color(0xFFF5F3FF)
            ) {
                Row(
                    modifier = Modifier.padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isOdiaSelected = false },
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                if (!isOdiaSelected) Color(0xFF7C3AED) else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Text(
                            text = "EN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isOdiaSelected) Color.White else Color(0xFF6D28D9)
                        )
                    }

                    IconButton(
                        onClick = { isOdiaSelected = true },
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                if (isOdiaSelected) Color(0xFF7C3AED) else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Text(
                            text = "ଓଡ଼ିଆ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isOdiaSelected) Color.White else Color(0xFF6D28D9)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Story Selector Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(stories.indices.toList()) { index ->
                val story = stories[index]
                FilterChip(
                    selected = selectedStoryIndex == index,
                    onClick = {
                        selectedStoryIndex = index
                        playbackProgressSeconds = 0f
                        isPlaying = true
                    },
                    label = {
                        Text(
                            text = if (isOdiaSelected) story.odiaTitle else story.title.take(18) + "...",
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(story.themeColor),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Audio Player Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
            border = BorderStroke(1.5.dp, Color(0xFFDDD6FE)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Waveform indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = if (isPlaying) Color(0xFF7C3AED) else Color(0xFFA78BFA),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPlaying) "Playing Audio Narration..." else "Audio Ready",
                        color = Color(0xFF6D28D9),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isOdiaSelected) currentStory.odiaTitle else currentStory.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E1065)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Slider
                Slider(
                    value = playbackProgressSeconds,
                    onValueChange = { playbackProgressSeconds = it },
                    valueRange = 0f..currentStory.durationSeconds.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF7C3AED),
                        activeTrackColor = Color(0xFF7C3AED),
                        inactiveTrackColor = Color(0xFFDDD6FE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val currentMin = (playbackProgressSeconds / 60).toInt()
                    val currentSec = (playbackProgressSeconds % 60).toInt()
                    val totalMin = currentStory.durationSeconds / 60
                    val totalSec = currentStory.durationSeconds % 60

                    Text(
                        text = String.format("%02d:%02d", currentMin, currentSec),
                        fontSize = 11.sp,
                        color = Color(0xFF6D28D9)
                    )
                    Text(
                        text = String.format("%02d:%02d", totalMin, totalSec),
                        fontSize = 11.sp,
                        color = Color(0xFF6D28D9)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Control Buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            playbackProgressSeconds = (playbackProgressSeconds - 10f).coerceAtLeast(0f)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Rewind 10s",
                            tint = Color(0xFF6D28D9),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF7C3AED),
                        modifier = Modifier.size(54.dp),
                        shadowElevation = 4.dp
                    ) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            playbackProgressSeconds = (playbackProgressSeconds + 10f).coerceAtMost(currentStory.durationSeconds.toFloat())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Forward 10s",
                            tint = Color(0xFF6D28D9),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Synchronized Text Transcript Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdiaSelected) "ଓଡ଼ିଆ ଅନୁବାଦ ଓ ଲିପି" else "Full Audio Story Transcript",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4C1D95)
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEDE9FE)
                    ) {
                        Text(
                            text = if (isOdiaSelected) "ଓଡ଼ିଆ" else "ENGLISH",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6D28D9)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isOdiaSelected) currentStory.odiaNarrative else currentStory.englishNarrative,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Temple Architecture AR & Rekha Deula Spatial Visualizer
        var isArModeActive by remember { mutableStateOf(false) }
        var arZoomLevel by remember { mutableFloatStateOf(1.0f) }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
            border = BorderStroke(1.dp, Color(0xFF6366F1))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏛️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Kalinga Architecture AR",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Rekha Deula • Jagamohana • Natamandira",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFA5B4FC),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Switch(
                        checked = isArModeActive,
                        onCheckedChange = {
                            isArModeActive = it
                            Toast.makeText(
                                context,
                                if (it) "AR View Activated: Point camera at open floor" else "AR View Closed",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF6366F1)
                        )
                    )
                }

                if (isArModeActive) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF312E81),
                        border = BorderStroke(1.dp, Color(0xFF4F46E5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "3D Architectural Projections",
                                    color = Color(0xFFC7D2FE),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Zoom: ${String.format(Locale.getDefault(), "%.1fx", arZoomLevel)}",
                                    color = Color(0xFFA5B4FC),
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            listOf(
                                "Vimana (Sanctum Tower): 78 ft height, curvilinear spire (Rekha style)",
                                "Amalaka & Kalasa: Crown stone and sacred pitcher pinnacle",
                                "Jagamohana (Audience Hall): Pyramidal Pidha Deula tier layout",
                                "Baulamala Stone: Hand-chiseled red sandstone relief carvings"
                            ).forEach { elem ->
                                Text(
                                    text = "• $elem",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = arZoomLevel,
                                onValueChange = { arZoomLevel = it },
                                valueRange = 0.5f..2.5f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF818CF8),
                                    activeTrackColor = Color(0xFF6366F1)
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done Listening", fontWeight = FontWeight.Bold)
        }
    }
}
