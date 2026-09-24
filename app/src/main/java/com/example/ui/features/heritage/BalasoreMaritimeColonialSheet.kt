package com.example.ui.features.heritage

import android.content.Intent
import android.net.Uri
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
import com.example.data.model.AppLanguage

data class ColonialHeritageSite(
    val siteName: String,
    val siteNameOd: String,
    val colonialPower: String, // "Dutch VOC", "French", "British EIC", "Danish"
    val eraCentury: String,
    val locationDescription: String,
    val historicalSignificance: String
)

val BALASORE_COLONIAL_SITES = listOf(
    ColonialHeritageSite(
        siteName = "Farasidinga (French Loge & Port Enclave)",
        siteNameOd = "ଫରାସୀଡିଙ୍ଗା (ଫ୍ରେଞ୍ଚ୍ ବାଣିଜ୍ୟ କେନ୍ଦ୍ର)",
        colonialPower = "French Colonial Enterprise (1673-1947)",
        eraCentury = "17th - 20th Century",
        locationDescription = "Near Barabati & Nunia Chhatak, Balasore Town",
        historicalSignificance = "A unique 38-acre French sovereign enclave inside British India until retroceded in 1947. Hosted French governors, salt merchants, and river docks."
    ),
    ColonialHeritageSite(
        siteName = "Old Dutch Cemetery & VOC Factory Site",
        siteNameOd = "ଓଲନ୍ଦାଜ୍ କବରସ୍ତାନ ଓ ଡଚ୍ କୋଠୀ",
        colonialPower = "Dutch East India Company (VOC)",
        eraCentury = "1625 - 1824 AD",
        locationDescription = "Barabati locality, Balasore",
        historicalSignificance = "Features pyramidal masonry Dutch obelisks and engraved gravestones of 17th-century merchant factor Michael Janszoon. Key depot for silk, saltpeter, and textiles."
    ),
    ColonialHeritageSite(
        siteName = "Dinamar Dinga (Danish Settlement)",
        siteNameOd = "ଦିନାମାର ଡିଙ୍ଗା (ଡେନମାର୍କ ବଣିକ ଘାଟ)",
        colonialPower = "Danish Asiatic Company (Tranquebar link)",
        eraCentury = "Late 17th Century",
        locationDescription = "Near Budhabalanga River curve, Balasore",
        historicalSignificance = "Trading station where Danish vessels exchanged copper and spices for Balasore fine muslin (Sanahs and Cassaes)."
    ),
    ColonialHeritageSite(
        siteName = "British Factory & Old Cemetery (1633 AD)",
        siteNameOd = "ଇଂରେଜ୍ ଫ୍ୟାକ୍ଟ୍ରି ଓ ପୁରାତନ କବରସ୍ତାନ",
        colonialPower = "British East India Company (First in Eastern India)",
        eraCentury = "Established 1633 AD by Ralph Cartwright",
        locationDescription = "Old Collectorate Road, Near Zilla School",
        historicalSignificance = "The very first British trading outpost in Eastern India, predating Calcutta (Kolkata). Contains Sir John Beames memorial landmarks and colonial civic offices."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreMaritimeColonialSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .testTag("balasore_maritime_colonial_sheet")
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
                            .background(Color(0xFFE0E7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⛵", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ସାମୁଦ୍ରିକ ଓ ଔପନିବେଶିକ ଇତିହାସ" else "Maritime & Colonial Port Heritage",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଫରାସୀଡିଙ୍ଗା • ଡଚ୍ କବର • ଇଂରେଜ୍ ଫ୍ୟାକ୍ଟ୍ରି (୧୬୩୩)" else "Farasidinga French Enclave • Dutch VOC Depots",
                            fontSize = 12.sp,
                            color = Color(0xFF4338CA)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF1F5F9),
                contentColor = Color(0xFF4338CA),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଔପନିବେଶିକ ସ୍ଥଳୀ" else "European Ports",
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
                            text = if (language == AppLanguage.ODIA) "ସାଧବ ବୋଇତ ବନ୍ଦାଣ" else "Ancient Sadhabas",
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
                        items(BALASORE_COLONIAL_SITES) { site ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) site.siteNameOd else site.siteName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = site.eraCentury,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4338CA),
                                            modifier = Modifier
                                                .background(Color(0xFFEEF2FF), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "🏛️ ${site.colonialPower}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF3730A3),
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                    Text(
                                        text = "📍 Location: ${site.locationDescription}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = site.historicalSignificance,
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFF93C5FD)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଆ-କା-ମା-ବୈ: କଳିଙ୍ଗର ବୋଇତ ବନ୍ଦାଣ ପରମ୍ପରା" else "Boita Bandana: Kalinga's Maritime Glory",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Centuries before European trading companies arrived, Sadhabas (ancient Odia sea merchants) sailed out of the Budhabalanga and Subarnarekha estuaries on Kartik Purnima aboard giant sea-going vessels (Boitas). They traded fine Balasore cotton, sea salt, iron implements, and spices with Suvarnabhumi (Sumatra, Java, Bali, and Sri Lanka).",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "📍 Celebrated annually at Balasore river ghats with the launching of miniature cork and banana bark boats.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF1D4ED8),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
