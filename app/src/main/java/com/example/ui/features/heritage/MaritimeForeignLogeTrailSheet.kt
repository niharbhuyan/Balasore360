package com.example.ui.features.heritage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

data class ForeignLogeSite(
    val id: String,
    val titleEn: String,
    val titleOd: String,
    val colonialPower: String,
    val establishmentYear: String,
    val locality: String,
    val historicalSignificanceEn: String,
    val historicalSignificanceOd: String,
    val currentHeritageStatus: String,
    val keyArtifacts: String
)

val DEFAULT_LOGE_SITES = listOf(
    ForeignLogeSite(
        id = "loge_1",
        titleEn = "Farashdanga French Loge & Salt Trading Post",
        titleOd = "ଫରାସୀଡଙ୍ଗା ଫ୍ରେଞ୍ଚ୍ ଲୋଜ୍ ଓ ଲୁଣ ବାଣିଜ୍ୟ କୋଠି",
        colonialPower = "French East India Company",
        establishmentYear = "1673 AD (Reconfirmed 1816)",
        locality = "Farashdanga, Budhabalanga Riverbank",
        historicalSignificanceEn = "A sovereign French enclave until 1947 inside British India. It served as a premier center for Balasore fine muslin and salt maritime commerce.",
        historicalSignificanceOd = "୧୯୪୭ ପର୍ଯ୍ୟନ୍ତ ଏହା ଫରାସୀ ସରକାରଙ୍କ ଅଧୀନରେ ଥିଲା। ସୂକ୍ଷ୍ମ ବସ୍ତ୍ର ଓ ଲୁଣ ବାଣିଜ୍ୟର ଏହା ମୁଖ୍ୟ କେନ୍ଦ୍ର ଥିଲା।",
        currentHeritageStatus = "State Protected Archaeological Enclave",
        keyArtifacts = "French boundary pillars, old customs jetty ruins & 18th-century trade ledgers"
    ),
    ForeignLogeSite(
        id = "loge_2",
        titleEn = "Dutch Cemetery & Olandaz Kothi",
        titleOd = "ଡଚ୍ ସମାଧିପୀଠ ଓ ଓଲନ୍ଦାଜ୍ କୋଠି",
        colonialPower = "Vereenigde Oostindische Compagnie (VOC - Dutch)",
        establishmentYear = "1650 AD (Cemetery 1675)",
        locality = "Barabati Ward, Balasore Town",
        historicalSignificanceEn = "Contains massive 17th-century obelisks and gravestones inscribed in archaic Dutch for VOC merchants who exported silk and saltpeter.",
        historicalSignificanceOd = "୧୭ଶ ଶତାବ୍ଦୀର ବିରାଟ ସ୍ମାରକୀ ସ୍ତମ୍ଭ। ଡଚ୍ ବ୍ୟବସାୟୀଙ୍କ ଇତିହାସ ଏଠାରେ ସଂରକ୍ଷିତ।",
        currentHeritageStatus = "ASI Protected Monument",
        keyArtifacts = "Carved stone obelisks of VOC Merchant Michael Jansz (1696) & Mrs. Beata Pieters"
    ),
    ForeignLogeSite(
        id = "loge_3",
        titleEn = "Danish Settlement & Dinemardanga Factory",
        titleOd = "ଦିନେମାରଡଙ୍ଗା ଡେନିସ୍ ବାଣିଜ୍ୟ କୋଠି",
        colonialPower = "Danish East India Company",
        establishmentYear = "1636 AD (Transferred 1845)",
        locality = "Dinemardanga, Balasore Port Reach",
        historicalSignificanceEn = "One of the earliest Scandinavian settlements in India, established by Christian IV's maritime expedition for pepper and Balasore calico cloth.",
        historicalSignificanceOd = "ଡେନମାର୍କର ପ୍ରାଚୀନ ବାଣିଜ୍ୟ ସ୍ଥଳୀ ଯେଉଁଠାରୁ ବାଲେଶ୍ୱରୀ ସୂତା କପଡ଼ା ୟୁରୋପକୁ ରପ୍ତାନି ହେଉଥିଲା।",
        currentHeritageStatus = "Heritage Trail Marker Installed",
        keyArtifacts = "River dock moorings, colonial brick foundations & coin finds"
    ),
    ForeignLogeSite(
        id = "loge_4",
        titleEn = "Old British Maritime Factory & Flagstaff Mount",
        titleOd = "ପୁରୁଣା ବ୍ରିଟିଶ୍ ଫେକ୍ଟ୍ରି ଓ ଫ୍ଲାଗଷ୍ଟାଫ୍ ମାଉଣ୍ଟ",
        colonialPower = "English East India Company",
        establishmentYear = "1642 AD",
        locality = "Barabati & Old Port Channel",
        historicalSignificanceEn = "The first British commercial factory in Eastern India, predating Calcutta. Served as Gabriel Boughton's medical trade base with Mughal Subahdars.",
        historicalSignificanceOd = "କଲିକତା ପୂର୍ବରୁ ପୂର୍ବ ଭାରତରେ ଇଷ୍ଟ ଇଣ୍ଡିଆ କମ୍ପାନୀର ପ୍ରଥମ ବାଣିଜ୍ୟିକ କୋଠି।",
        currentHeritageStatus = "Historic Plaque & Museum Walk",
        keyArtifacts = "Historic 17th-century brass cannon, ship bell & maritime trade maps"
    )
)

/**
 * Maritime Heritage & French-Danish-Dutch Loge Historic Trail Sheet.
 */
@Composable
fun MaritimeForeignLogeTrailSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    var playingAudioId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("maritime_foreign_loge_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF334155), OceanBlue)
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
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF38BDF8).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "MARITIME COLONIAL HERITAGE • 1636-1947",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF7DD3FC)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ଫରାସୀ, ଡଚ୍ ଓ ଡେନିସ୍ ବାଣିଜ୍ୟ କୋଠି ଐତିହ୍ୟ" else "Balasore Foreign Loge & Maritime Trail",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଫରାସୀଡଙ୍ଗା, ଡଚ୍ ସମାଧି (୧୬୭୫) ଓ ଦିନେମାରଡଙ୍ଗାରେ ବାଲେଶ୍ୱରର ଆନ୍ତର୍ଜାତୀୟ ବାଣିଜ୍ୟ ଇତିହାସ"
                        else
                            "Explore Farashdanga French enclave, Dutch obelisks (1675), and Danish maritime spice warehouses along Budhabalanga river.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        items(DEFAULT_LOGE_SITES) { site ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) site.titleOd else site.titleEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "${site.colonialPower} • Est. ${site.establishmentYear}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OceanBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        IconButton(
                            onClick = {
                                playingAudioId = if (playingAudioId == site.id) null else site.id
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (playingAudioId == site.id) EmeraldGreen else OceanBlue.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Audio Guide",
                                tint = if (playingAudioId == site.id) Color.White else OceanBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) site.historicalSignificanceOd else site.historicalSignificanceEn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoSlate700,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BentoSlate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(site.locality, fontSize = 11.sp, color = BentoSlate600)
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BentoSlate100,
                            border = BorderStroke(1.dp, BentoSlate200)
                        ) {
                            Text(
                                text = site.currentHeritageStatus,
                                color = BentoSlate800,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
