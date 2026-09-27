package com.example.ui.features.artisans

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
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class DokraMasterArtisan(
    val nameEn: String,
    val nameOd: String,
    val villageCluster: String,
    val craftSpecialty: String,
    val phoneContact: String,
    val nationalAwardWinner: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NilagiriDokraCraftGuildSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val artisans = listOf(
        DokraMasterArtisan(
            nameEn = "Sarat Kumar Rana (Master Metal Caster)",
            nameOd = "ଶରତ କୁମାର ରାଣା (ପ୍ରମୁଖ ଡୋକ୍ରା ଶିଳ୍ପୀ)",
            villageCluster = "Mitrapur, Nilagiri Block",
            craftSpecialty = "Traditional Elephants, Peacock Lamps & Tribal Figurines",
            phoneContact = "+919437108920",
            nationalAwardWinner = true
        ),
        DokraMasterArtisan(
            nameEn = "Subashini Mahanta (Lost-Wax Artisan)",
            nameOd = "ସୁବାସିନୀ ମହାନ୍ତ (ମହୁମାଛି ମହମ କାରିଗର)",
            villageCluster = "Tinikosia Forest Fringe, Nilagiri",
            craftSpecialty = "Dokra Jewelry, Pendants & Deity Idols",
            phoneContact = "+919861054321",
            nationalAwardWinner = false
        ),
        DokraMasterArtisan(
            nameEn = "Bikram Behera (Bell Metal Craftsman)",
            nameOd = "ବିକ୍ରମ ବେହେରା (କଂସା-ପିତ୍ତଳ ଶିଳ୍ପୀ)",
            villageCluster = "Oupada Tribal Cluster",
            craftSpecialty = "Ritual Bells, Betel Nut Cutters & Ancient Measuring Bowls",
            phoneContact = "+919938221144",
            nationalAwardWinner = false
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
                .testTag("nilagiri_dokra_sheet")
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
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Dokra Icon",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ଡୋକ୍ରା ଓ କଂସା-ପିତ୍ତଳ ଗିଲ୍ଡ" else "Nilagiri Dokra & Tribal Metal Craft",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମଧୁମହମ ହଜିଲା ପଦ୍ଧତି ଓ ସିଧା ଶିଳ୍ପୀ ସଂଯୋଗ" else "4,000-Year-Old Lost-Wax Metal Casting",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("dokra_close_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guild Telemetry Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସକ୍ରିୟ ପଞ୍ଜୀକୃତ ଶିଳ୍ପୀ ସଂଘ" else "Active Verified Artisans",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        )
                        Text(
                            text = "${dailyPulse.activeDokraArtisansCount} Master Craftsmen",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFB45309)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Exhibition: ${dailyPulse.upcomingDokraExhibition} • 100% Middleman-Free Direct Fair Trade",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = Color(0xFF78350F)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ପ୍ରମୁଖ ନୀଳଗିରି ମାଷ୍ଟର ଶିଳ୍ପୀ ଡାଇରେକ୍ଟୋରି" else "Nilagiri Master Artisan Directory",
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
                items(artisans) { art ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, BentoSlate200),
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
                                        text = if (language == AppLanguage.ODIA) art.nameOd else art.nameEn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = "📍 ${art.villageCluster}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = BentoSlate600
                                        )
                                    )
                                }

                                if (art.nationalAwardWinner) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFEF3C7),
                                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                                    ) {
                                        Text(
                                            text = "AWARDED",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "🎨 ${art.craftSpecialty}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = BentoSlate700
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("https://wa.me/${art.phoneContact.replace("+", "")}?text=Hello,%20I%20am%20interested%20in%20Nilagiri%20Dokra%20crafts.")
                                        }
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Chat, contentDescription = "WhatsApp", modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("WhatsApp", fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${art.phoneContact}")
                                        }
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call Artisan", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
