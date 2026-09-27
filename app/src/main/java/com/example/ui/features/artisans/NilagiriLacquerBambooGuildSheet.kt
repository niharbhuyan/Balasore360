package com.example.ui.features.artisans

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

/**
 * Royal Nilagiri Lacquer Painted Craft & Swarna Rekha Bamboo Guild.
 * Connects heritage buyers directly to Nilagiri, Motiganj & Remuna master artisans crafting
 * traditional lacquer bangles (ଲାଖ ଶଙ୍ଖା), lacquered wooden toys, and eco-bamboo weaving.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NilagiriLacquerBambooGuildSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("nilagiri_lacquer_bamboo_guild_sheet")
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
                        Text(text = "🌾", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ଲାଖ ଶିଳ୍ପ ଓ ବାଉଁଶ ଗିଲ୍ଡ" else "Nilagiri Lacquer & Bamboo Guild",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରାଜକୀୟ ଲାଖ ଶଙ୍ଖା, ଖେଳଣା ଓ ସ୍ୱର୍ଣ୍ଣରେଖା ବାଉଁଶ ହସ୍ତଶିଳ୍ପ" else "Royal Lacquer Bangles, Toys & Bamboo SHG Clusters",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("btn_close_lacquer_sheet")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guild Telemetry Banner
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
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF59E0B))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ACTIVE ARTISAN TELEMETRY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFDE68A),
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${dailyPulse.nilagiriLacquerArtisansCount} Master Lacquer Artisans • ${dailyPulse.nilagiriBambooClustersActive} Bamboo SHGs",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Daily Orders Placed: ${dailyPulse.nilagiriLacquerBangleOrdersToday} • 100% Zero-Middleman Guarantee",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରୀ ଲାଖ ଶଙ୍ଖା (Lakha Shankha) ପରମ୍ପରା" else "Heritage Balasore Lacquer Bangles",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "ନୀଳଗିରି ରାଜପରିବାର ଦ୍ୱାରା ପୃଷ୍ଠପୋଷକତା ପ୍ରାପ୍ତ ଲାଖ ଶଙ୍ଖା ସୁବର୍ଣ୍ଣ ଓ ଚାନ୍ଦି ରଙ୍ଗରେ କାରୁକାର୍ଯ୍ୟ ହୋଇଥାଏ। ଏହା ଓଡ଼ିଶାର ଏକ ପ୍ରସିଦ୍ଧ ପାରମ୍ପରିକ ବିବାହ ଅଳଙ୍କାର।"
                                else
                                    "Patronized by Nilagiri royal court, these handcrafted lacquer bangles are embellished with natural mineral pigments, gold dust, and mirror inlays. Authentic wedding heritage of North Odisha.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ସୁବର୍ଣ୍ଣରେଖା ବାଉଁଶ ଶିଳ୍ପ ଓ ମିଶନ ଶକ୍ତି କ୍ଲଷ୍ଟର" else "Swarna Rekha Bamboo & Mission Shakti SHGs",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Bhograi & Jaleswar riparian bamboo artisan federations weaving eco-friendly baskets, lamp shades, fruit trays, and organic furniture. Backed by Odisha Mission Shakti.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262234"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_call_artisan_guild"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Artisan Help Desk", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("geo:21.4600,86.7600?q=Nilagiri+Artisan+Village+Balasore")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_navigate_nilagiri_craft"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Guild Cluster Map", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}
