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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

/**
 * 13th-century Raibania Fort complex built by Eastern Ganga Emperor Narasimhadeva I.
 * Eastern India's largest medieval stone fortress with 161 moats, bastions, Jayachandi temple,
 * and subterranean archaeological remnants near Jaleswar, Balasore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaibaniaMedievalFortressVaultSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("raibania_medieval_fortress_vault_sheet")
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏰", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରାଇବଣିଆ ଦୁର୍ଗ ପ୍ରତ୍ନତାତ୍ତ୍ୱିକ ଭଲ୍ଟ" else "Raibania Medieval Fortress Vault",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "୧୩ଶ ଶତାବ୍ଦୀ ଗଙ୍ଗ ବଂଶ ପ୍ରସ୍ତର ଦୁର୍ଗ ଓ ୧୬୧ ଗଡ଼ଖାଇ" else "13th-Century Eastern Ganga Stronghold • 161 Moats",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("btn_close_raibania_sheet")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live Telemetry Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DAILY ARCHEOLOGICAL PULSE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8),
                                    letterSpacing = 1.sp,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dailyPulse.raibaniaExcavationStatus,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Golden Hour Window: ${dailyPulse.raibaniaGoldenHourPhotoWindow} • Visitors Today: ${dailyPulse.raibaniaDailyVisitorsCount}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Tabs
            val tabs = listOf(
                if (language == AppLanguage.ODIA) "ଦୁର୍ଗ ସଂରଚନା" else "Architecture",
                if (language == AppLanguage.ODIA) "୧୬୧ ଗଡ଼ଖାଇ" else "161 Moats",
                if (language == AppLanguage.ODIA) "ଜୟଚଣ୍ଡୀ ପୀଠ" else "Jayachandi Shrine",
                if (language == AppLanguage.ODIA) "ଗାଇଡ୍ ଓ ମାର୍ଗ" else "GPS Route"
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = OceanBlueDark,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                when (selectedTab) {
                    0 -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                                border = BorderStroke(1.dp, Color(0xFFFDE68A))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ପୂର୍ବ ଗଙ୍ଗ ସମ୍ରାଟ ନରସିଂହଦେବଙ୍କ ପ୍ରସ୍ତର ପ୍ରାଚୀର" else "Ganga Empire Fortress Architecture",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA)
                                            "୧୨୩୮-୧୨୬୪ ଖ୍ରୀଷ୍ଟାବ୍ଦରେ କୋଣାର୍କ ସୂର୍ଯ୍ୟ ମନ୍ଦିର ନିର୍ମାତା ଲାଙ୍ଗୁଳା ନରସିଂହଦେବ ଉତ୍ତର ଓଡ଼ିଶା ସୀମାନ୍ତକୁ ମୁସଲିମ ଆକ୍ରମଣରୁ ରକ୍ଷା କରିବା ପାଇଁ ଏହି ବିଶାଳ ଦୁର୍ଗ ନିର୍ମାଣ କରିଥିଲେ। ଏଥିରେ ଲକ୍ଷାଧିକ ସୈନ୍ୟ ରହିବାର କ୍ଷମତା ଥିଲା।"
                                        else
                                            "Constructed between 1238–1264 AD by Langula Narasimhadeva I (builder of Konark), Raibania stood as Odisha's premier northern bulwark. Its cyclopean laterite stone walls and ramparts housed an entire imperial cavalry.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate600,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "🛡️ 161 Bastions", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = "Double concentric rings", fontSize = 11.sp, color = BentoSlate500)
                                    }
                                }
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "📐 6 Sq Km", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = "Largest stone fort area", fontSize = 11.sp, color = BentoSlate500)
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ସୁବର୍ଣ୍ଣରେଖା ଜଳଚକ୍ର ଓ ଗଡ଼ଖାଇ ପ୍ରଣାଳୀ" else "Hydro-Engineering & 161 Defense Moats",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA)
                                            "ରାଇବଣିଆରେ ୧୬୧ଟି ଗଡ଼ଖାଇ ଓ ପୋଖରୀ ଖନନ କରାଯାଇଥିଲା। ସୁବର୍ଣ୍ଣରେଖା ନଦୀରୁ ଜଳ ଆଣି ଶତ୍ରୁ ସେନାଙ୍କୁ ଅଟକାଇବା ପାଇଁ ଏହି ଜଳଦୁର୍ଗ ବ୍ୟବହୃତ ହେଉଥିଲା।"
                                        else
                                            "The fort was defended by an intricate labyrinth of 161 moats and large reservoirs connected to the Subarnarekha river, rendering it virtually impregnable to medieval siege engines.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                                border = BorderStroke(1.dp, Color(0xFFFED7AA))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ମା' ଜୟଚଣ୍ଡୀ ମନ୍ଦିର ଓ ଐତିହାସିକ ବିଗ୍ରହ" else "Maa Jayachandi Sanctum",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA)
                                            "ଦୁର୍ଗ ମଧ୍ୟରେ ଅଧିଷ୍ଠାତ୍ରୀ ଦେବୀ ମା' ଜୟଚଣ୍ଡୀଙ୍କ ପୀଠ ବିଦ୍ୟମାନ। ପୂଜା ପରମ୍ପରା ଆଜି ମଧ୍ୟ ଜାରି ରହିଛି।"
                                        else
                                            "The spiritual heart of Raibania houses Goddess Jayachandi, presiding deity of the Ganga warriors. Ritual pujas continue daily amidst laterite ruins.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଜଳେଶ୍ୱର ଠାରୁ ରାଇବଣିଆ ଯିବା ବାଟ" else "Getting to Raibania from Jaleswar / Balasore",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Distance: 14 km from Jaleswar Railway Station • 65 km from Balasore HQ along NH-16. Accessible via auto-rickshaw or personal cab.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("geo:21.9214,87.1953?q=Raibania+Fort+Balasore")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_navigate_raibania"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Open GPS Map", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Raibania Medieval Fortress - Balasore")
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Explore Raibania Fort: Eastern India's largest 13th-century stone fortress built by King Narasimhadeva I in Balasore district! Download Balasore 360 app."
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Raibania Fort"))
                            },
                            modifier = Modifier.weight(1f).testTag("btn_share_raibania"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Share Vault", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}
