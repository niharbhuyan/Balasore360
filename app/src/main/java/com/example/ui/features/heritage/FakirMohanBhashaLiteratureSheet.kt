package com.example.ui.features.heritage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.data.model.AppLanguage
import java.text.SimpleDateFormat
import java.util.*

data class BaleswariIdiom(
    val phraseOdia: String,
    val phraseEnglishTransliteration: String,
    val meaningOdia: String,
    val meaningEnglish: String,
    val contextualExample: String
)

data class HeritageMemorialSite(
    val siteName: String,
    val siteNameOd: String,
    val location: String,
    val significance: String,
    val visitingHours: String
)

val BALESWARI_IDIOMS = listOf(
    BaleswariIdiom(
        phraseOdia = "ହାତୀ ଗଲେ ପିମ୍ପୁଡ଼ି କହେ ରାସ୍ତା ଛାଡ଼",
        phraseEnglishTransliteration = "Hati gale pimpudi kahe rasta chhada",
        meaningOdia = "ଅତି ଦୁର୍ବଳ ବ୍ୟକ୍ତି ନିଜ କ୍ଷମତା ନଥାଇ ବଡ଼ ଲୋକଙ୍କୁ ଆଦେଶ ଦେବା।",
        meaningEnglish = "An ant commanding an elephant to make way; punchy Balasore folk satire on false bravado.",
        contextualExample = "Used affectionately in village chaupals to tease overambitious talk."
    ),
    BaleswariIdiom(
        phraseOdia = "ପେଟରେ ନାହିଁ ଦାନା, ମୁଣ୍ଡରେ କୁସୁମ ତେଲ",
        phraseEnglishTransliteration = "Petare nahin dana, mundare kusuma tela",
        meaningOdia = "ନିଜର ପ୍ରାଥମିକ ଆବଶ୍ୟକତା ନଥାଇ ଅଯଥା ବିଳାସିତା ଦେଖାଇବା।",
        meaningEnglish = "Hungry stomach yet perfumed hair oil; highlighting superficial vanity.",
        contextualExample = "Classic Fakir Mohan dialect realism portraying village socio-economics."
    ),
    BaleswariIdiom(
        phraseOdia = "ଗଛ ନ ପାଚୁଣୁ ପାଣି ପିଇବା",
        phraseEnglishTransliteration = "Gachha na pachunu pani pieba",
        meaningOdia = "କାର୍ଯ୍ୟ ସମ୍ପନ୍ନ ନହେଉଣୁ ଫଳ ଭୋଗ କରିବାକୁ ଅତ୍ୟନ୍ତ ବ୍ୟଗ୍ର ହେବା।",
        meaningEnglish = "Drinking water before the fruit on the tree even ripens; counting chickens before they hatch.",
        contextualExample = "Frequently cited by elders regarding harvest impatience."
    )
)

val LITERARY_SITES = listOf(
    HeritageMemorialSite(
        siteName = "Vyasakabi Fakir Mohan Smruti Peetha (Shantisneha)",
        siteNameOd = "ବ୍ୟାସକବି ଫକୀର ମୋହନ ସ୍ମୃତି ପୀଠ (ଶାନ୍ତିସ୍ନେହ)",
        location = "Mallikashpur, Balasore (Near FM College)",
        significance = "Ancestral residence of the Father of Modern Odia Fiction, author of 'Chha Mana Atha Guntha' & 'Rebati'. Original writing desk preserved.",
        visitingHours = "09:30 AM - 05:00 PM (Monday Closed)"
    ),
    HeritageMemorialSite(
        siteName = "Bodhadayini Press Historic Printing Site",
        siteNameOd = "ବୋଧଦାୟିନୀ ପ୍ରେସ ଐତିହାସିକ ସ୍ଥଳୀ",
        location = "Motiganj Bazar, Balasore",
        significance = "Established in 1868; first modern printing press in North Odisha that triggered the historic Odia language renaissance.",
        visitingHours = "Open Access Memorial Plaque"
    ),
    HeritageMemorialSite(
        siteName = "Balasore District Central Library",
        siteNameOd = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା କେନ୍ଦ୍ରୀୟ ପାଠାଗାର",
        location = "Raja Krushna Chandra High School Road",
        significance = "Over 45,000 volumes including rare 19th-century palm-leaf manuscripts, Utkal Deepika archives, and Fakir Mohan original publications.",
        visitingHours = "10:00 AM - 06:00 PM (Reading Room Free Entry)"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakirMohanBhashaLiteratureSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var syncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        syncTime = sdf.format(Date())
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .testTag("fakir_mohan_literature_sheet")
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
                        Text("📜", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଫକୀର ମୋହନ ଭାଷା ଓ ଓଡ଼ିଆ ସାହିତ୍ୟ ଆର୍କାଇଭ" else "Fakir Mohan Bhasha & Literature Hub",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଶାନ୍ତିସ୍ନେହ ମଲ୍ଲିକାଶପୁର • ବାଲେଶ୍ୱରୀ ଢଗଢମାଳି • ପାଠାଗାର" else "Mallikashpur Peetha • Baleswari Idioms • Archives",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sync pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD97706))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Daily literary heritage active ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "ODIA CRADLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFFB45309),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରୀ ଢଗଢମାଳି" else "Folk Idioms",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସାହିତ୍ୟ ସ୍ମୃତିପୀଠ" else "Memorial Sites",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପାଠାଗାର କ୍ୟାଟାଲଗ୍" else "Library Access",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(BALESWARI_IDIOMS) { idiom ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "“${idiom.phraseOdia}”",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                    Text(
                                        text = idiom.phraseEnglishTransliteration,
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "💡 Odia: ${idiom.meaningOdia}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF0F172A),
                                        lineHeight = 16.sp
                                    )
                                    Text(
                                        text = "🌐 English: ${idiom.meaningEnglish}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 15.sp,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📖 Context: ${idiom.contextualExample}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(LITERARY_SITES) { site ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) site.siteNameOd else site.siteName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "📍 ${site.location}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = site.significance,
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🕒 Hours: ${site.visitingHours}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF166534),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଓଡ଼ିଆ ଭାଷା ଓ ଇ-ପାଠାଗାର ସୁବିଧା" else "District Public Library Free Membership",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Free public access to competitive exam books (OPSC, UPSC, SSC, Banking) in reading hall.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Digitized archives of Fakir Mohan's Sambalpur Hiteishini and Bodhadayini available on LAN terminals.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Annual Fakir Mohan Jayanti Celebrations held every 14th January.", fontSize = 12.sp, color = Color(0xFF14532D))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://fmuniversity.ac.in"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Explore, contentDescription = "Web", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Visit Fakir Mohan University Language Portal", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
