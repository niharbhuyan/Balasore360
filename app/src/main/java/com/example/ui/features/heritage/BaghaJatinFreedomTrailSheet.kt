package com.example.ui.features.heritage

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*
import java.util.Locale

data class FreedomTrailStop(
    val titleEn: String,
    val titleOd: String,
    val yearHistorical: String,
    val descriptionEn: String,
    val descriptionOd: String,
    val audioScript: String,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaghaJatinFreedomTrailSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var currentlyPlayingTitle by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val stops = listOf(
        FreedomTrailStop(
            titleEn = "1. Kaptipada Forest Hideout & Secret Base",
            titleOd = "୧. କପ୍ତିପଦା ଘଞ୍ଚ ଜଙ୍ଗଲ ଆଶ୍ରୟସ୍ଥଳୀ",
            yearHistorical = "August 1915",
            descriptionEn = "Jatindranath Mukherjee and four comrades (Chittapriya, Manoranjan, Niren, Jyotish) prepared to receive German arms from the ship SS Maverick.",
            descriptionOd = "ଜତୀନ୍ଦ୍ରନାଥ ମୁଖାର୍ଜୀ ଓ ତାଙ୍କ ସାଥୀମାନେ ଜର୍ମାନ ଅସ୍ତ୍ରଶସ୍ତ୍ର ଆଗମନକୁ ଅପେକ୍ଷା କରି ଗୁପ୍ତ ଡେରା ପକାଇଥିଲେ।",
            audioScript = "Kaptipada Forest Hideout. Here in August 1915, revolutionary Bagha Jatin coordinated the historic plan for Indian armed liberation.",
            latitude = 21.5120,
            longitude = 86.5320
        ),
        FreedomTrailStop(
            titleEn = "2. Chasakhand Battlefield Memorial",
            titleOd = "୨. ଚାଷାଖଣ୍ଡ ସଂଗ୍ରାମ ସ୍ମୃତିପୀଠ (ବୁଢ଼ାବଳଙ୍ଗ ତଟ)",
            yearHistorical = "9 September 1915",
            descriptionEn = "A legendary 75-minute gun battle between five Indian revolutionaries armed with Mauser pistols and 300 heavily armed British police under Magistrate Kilby.",
            descriptionOd = "ମାଉଜର୍ ପିସ୍ତଲ ସାହାଯ୍ୟରେ ପାଞ୍ଚଜଣ ବିପ୍ଳବୀ ବ୍ରିଟିଶ ସେନାବାହିନୀ ସହିତ ୭୫ ମିନିଟ୍ ଧରି ଐତିହାସିକ ଯୁଦ୍ଧ କରିଥିଲେ।",
            audioScript = "Chasakhand memorial on the banks of Budhabalanga river. In this trench, young Chittapriya Ray Chaudhuri achieved martyrdom.",
            latitude = 21.4920,
            longitude = 86.9240
        ),
        FreedomTrailStop(
            titleEn = "3. Balasore Old District Hospital Ward",
            titleOd = "୩. ପୁରୁଣା ବାଲେଶ୍ୱର ଜିଲ୍ଲା ଚିକିତ୍ସାଳୟ କକ୍ଷ",
            yearHistorical = "10 September 1915",
            descriptionEn = "Bagha Jatin took his last breath here murmuring: 'Amra morbo, desh jagbe' (We shall die, so the nation shall awake).",
            descriptionOd = "ଆମେ ମରିବୁ, ଦେଶ ଜାଗ୍ରତ ହେବ - ଏହି ଅମର ବାଣୀ ଉଚ୍ଚାରଣ କରି ବାଘା ଯତୀନ ଶେଷ ନିଶ୍ୱାସ ତ୍ୟାଗ କରିଥିଲେ।",
            audioScript = "Balasore Old Hospital Ward. Bagha Jatin breathed his last here, immortalizing the battle cry: We shall die, the nation shall awake.",
            latitude = 21.4945,
            longitude = 86.9325
        ),
        FreedomTrailStop(
            titleEn = "4. Barabati Freedom Fighter Memorial Park",
            titleOd = "୪. ବାରବାଟୀ ସ୍ୱାଧୀନତା ସଂଗ୍ରାମୀ ସ୍ମୃତି ଉଦ୍ୟାନ",
            yearHistorical = "Memorial Peetha",
            descriptionEn = "Statues of the five brave revolutionaries, historical battle exhibits, and archival court transcripts.",
            descriptionOd = "ପାଞ୍ଚ ସହିଦଙ୍କ ପୂର୍ଣ୍ଣାବୟବ ପ୍ରତିମୂର୍ତ୍ତି ଓ ସଂଗ୍ରହାଳୟ।",
            audioScript = "Barabati Freedom Fighter Memorial Park in Balasore town honors the supreme sacrifice of the Chasakhand martyrs.",
            latitude = 21.4910,
            longitude = 86.9380
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("bagha_jatin_trail_sheet")
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
                            .background(Color(0xFFFEF2F2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Freedom Trail Icon",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଘା ଯତୀନ ଓ ଚାଷାଖଣ୍ଡ ସ୍ୱାଧୀନତା ଟ୍ରେଲ୍" else "Bagha Jatin & Chasakhand Trail",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "୧୯୧୫ ଐତିହାସିକ ବୁଢ଼ାବଳଙ୍ଗ ଯୁଦ୍ଧ ଓ ଅଡିଓ ଗାଇଡ୍" else "1915 Battle of Chasakhand GPS Audio Walk",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("bagha_jatin_close_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Heritage Quote Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "“ଆମେ ମରିବୁ, ଦେଶ ଜାଗ୍ରତ ହେବ।” — ବାଘା ଯତୀନ (୯ ସେପ୍ଟେମ୍ବର ୧୯୧୫)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF9A3412)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Today's Visitors to Chasakhand Memorial: ${dailyPulse.chasakhandMemorialVisitorsToday} Pilgrims • 5 Audio Stops Active",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = Color(0xFFC2410C)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ଚାଷାଖଣ୍ଡ ଯୁଦ୍ଧର ଐତିହାସିକ ସ୍ଥାନ ଓ ଅଡିଓ" else "Historical Battle Landmarks & Audio Stories",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate800
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stops) { stop ->
                    val isPlaying = currentlyPlayingTitle == stop.titleEn

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isPlaying) Color(0xFFFEF2F2) else Color(0xFFF8FAFC)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isPlaying) Color(0xFFFCA5A5) else BentoSlate200
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) stop.titleOd else stop.titleEn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = "📅 ${stop.yearHistorical}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = BentoSlate600
                                        )
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            if (isPlaying) {
                                                tts?.stop()
                                                currentlyPlayingTitle = null
                                            } else {
                                                tts?.stop()
                                                currentlyPlayingTitle = stop.titleEn
                                                tts?.speak(stop.audioScript, TextToSpeech.QUEUE_FLUSH, null, "stop_${stop.titleEn}")
                                            }
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Default.StopCircle else Icons.Default.VolumeUp,
                                            contentDescription = "Play Audio",
                                            tint = if (isPlaying) Color(0xFFDC2626) else OceanBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            val uri = Uri.parse("geo:${stop.latitude},${stop.longitude}?q=${stop.latitude},${stop.longitude}(${Uri.encode(stop.titleEn)})")
                                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                            context.startActivity(mapIntent)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Navigation,
                                            contentDescription = "Navigate",
                                            tint = OceanBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (language == AppLanguage.ODIA) stop.descriptionOd else stop.descriptionEn,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = BentoSlate700
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
