package com.example.ui.features.heritage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class FestivalEvent(
    val titleEn: String,
    val titleOd: String,
    val venue: String,
    val venueOd: String,
    val monthPeriod: String,
    val significance: String,
    val ritualHighlights: String,
    val specialTransit: String
)

val BALASORE_FESTIVALS = listOf(
    FestivalEvent(
        titleEn = "Chandaneswar Chadak Mela & Uda Parba",
        titleOd = "ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ମେଳା ଓ ଉଡ଼ା ପର୍ବ",
        venue = "Chandaneswar Shiva Temple, Bhograi",
        venueOd = "ଚନ୍ଦନେଶ୍ୱର ଶୈବପୀଠ, ଭୋଗରାଇ",
        monthPeriod = "Chaitra Pana Sankranti (April)",
        significance = "Massive 13-day penance where thousands of Bhaktas (devotees) fast, walk on burning embers, and perform ancient Shiva tantric rites.",
        ritualHighlights = "Nila Parba, Kamana Ghat sthapana, and thrilling Uda swing ritual on high bamboo scaffolds.",
        specialTransit = "Special OSRTC pilgrim buses from Balasore Station, Jaleswar, and Digha."
    ),
    FestivalEvent(
        titleEn = "Baleswar Mahotsav (District Cultural Expo)",
        titleOd = "ବାଲେଶ୍ୱର ମହୋତ୍ସବ (ଜିଲ୍ଲା ସାଂସ୍କୃତିକ ମହୋତ୍ସବ)",
        venue = "ITI & Expo Grounds, Balasore",
        venueOd = "ଆଇଟିଆଇ ପ୍ରଦର୍ଶନୀ ପଡ଼ିଆ, ବାଲେଶ୍ୱର",
        monthPeriod = "Winter Carnival (December - January)",
        significance = "Grand 7-day cultural confluence celebrating Odia literature, tribal dance, live music, and maritime trade heritage.",
        ritualHighlights = "Pallishree Mela handicrafts, Sambalpuri & Odissi performances, and local food pavilion.",
        specialTransit = "Dedicated municipal shuttle buses connecting Cinema Chhak to ITI Field."
    ),
    FestivalEvent(
        titleEn = "Kartik Purnima Boita Bandana at Talasari",
        titleOd = "କାର୍ତ୍ତିକ ପୂର୍ଣ୍ଣିମା ବୋଇତ ବନ୍ଦାଣ (ତାଳସାରୀ)",
        venue = "Talasari Estuary & Subarnarekha Mouth",
        venueOd = "ତାଳସାରୀ ମୁହାଣ ଓ ସୁବର୍ଣ୍ଣରେଖା ସଙ୍ଗମ",
        monthPeriod = "Kartik Purnima Dawn (Nov)",
        significance = "Ancient maritime revival festival celebrating Sadhabas sailing to Java, Sumatra, and Bali for maritime trade.",
        ritualHighlights = "Dawn floating of miniature decorative boats with betel leaves, betel nuts, and burning earthen lamps.",
        specialTransit = "Early morning eco-taxis from Jaleswar and Chandaneswar."
    ),
    FestivalEvent(
        titleEn = "Remuna Khirachora Gopinath Chandan Yatra",
        titleOd = "ରେମୁଣା ଖିରଚୋରା ଗୋପୀନାଥ ଚନ୍ଦନ ଯାତ୍ରା",
        venue = "Khirachora Gopinath Temple, Remuna",
        venueOd = "ଖିରଚୋରା ଗୋପୀନାଥ ପୀଠ, ରେମୁଣା",
        monthPeriod = "Akshaya Tritiya to Jyestha (May - June)",
        significance = "42-day cooling water festival where deities cruise on swan-shaped boats in sacred temple tanks.",
        ritualHighlights = "Daily special Amruta Keli distribution, sandalwood paste abhishek, and evening Raas sankirtan.",
        specialTransit = "Toto e-rickshaws every 3 mins from Balasore Railway Station."
    )
)

/**
 * Baleswar Mahotsav & Festival Calendar.
 * Cultural schedules, ritual timetables for Chandaneswar Chadak Mela,
 * Boita Bandana, and Remuna temple festivals with pilgrim transit info.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaleswarMahotsavChadakSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All Festivals") }
    val filters = listOf("All Festivals", "Chadak Mela", "Winter Mahotsav", "Boita Bandana")

    val displayedFestivals = remember(selectedFilter) {
        when (selectedFilter) {
            "Chadak Mela" -> BALASORE_FESTIVALS.filter { it.titleEn.contains("Chadak", ignoreCase = true) }
            "Winter Mahotsav" -> BALASORE_FESTIVALS.filter { it.titleEn.contains("Mahotsav", ignoreCase = true) }
            "Boita Bandana" -> BALASORE_FESTIVALS.filter { it.titleEn.contains("Boita", ignoreCase = true) }
            else -> BALASORE_FESTIVALS
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("baleswar_mahotsav_chadak_sheet")
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎭", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମହୋତ୍ସବ ଓ ଚଡ଼କ କ୍ୟାଲେଣ୍ଡର" else "Baleswar Mahotsav & Festival Guide",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚନ୍ଦନେଶ୍ୱର ଉଡ଼ା ପର୍ବ • ବୋଇତ ବନ୍ଦାଣ • ସାଂସ୍କୃତିକ ମେଳା" else "Chadak Mela • Boita Bandana • Cultural Expo",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Filter Chips
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFD97706),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Festival Cards
                items(displayedFestivals) { festival ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        color = Color(0xFFFEF3C7),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = festival.monthPeriod,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF92400E)
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA) festival.titleOd else festival.titleEn,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = "📍 ${if (language == AppLanguage.ODIA) festival.venueOd else festival.venue}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF0369A1),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = festival.significance,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate700,
                                    lineHeight = 18.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF8FAFC)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "✨ Rituals: ${festival.ritualHighlights}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate800,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🚌 Transit: ${festival.specialTransit}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF0F766E),
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
