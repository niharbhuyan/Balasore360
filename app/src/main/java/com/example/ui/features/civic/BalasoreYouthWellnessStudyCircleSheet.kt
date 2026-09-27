package com.example.ui.features.civic

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
 * Balasore Youth Mental Wellness & Aspirant Study Circle.
 * Offers Tele-MANAS (14416) confidential tele-counselling desk, FM MCH Psychiatry chamber info,
 * Odia calming nature soundscapes (Chandipur tide, Remuna morning chimes), and peer study circles
 * for competitive exam aspirants (OPSC, UPSC, Odisha Police & Banking).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreYouthWellnessStudyCircleSheet(
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
                .testTag("balasore_youth_wellness_study_circle_sheet")
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
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧘", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଯୁବ ମାନସିକ ସ୍ୱାସ୍ଥ୍ୟ ଓ ଅଧ୍ୟୟନ ସର୍କଲ" else "Youth Wellness & Study Circle",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଟେଲି-ମାନସ (୧୪୪୧୬) ଓ ବାଲେଶ୍ୱର ପ୍ରତିଯୋଗିତା ଅଧ୍ୟୟନ ହବ୍" else "Tele-MANAS (14416) & Competitive Exam Circle",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("btn_close_wellness_sheet")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Community Telemetry Banner
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
                                    .background(Color(0xFFA855F7))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "STUDENT & ASPIRANT CIRCLE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC084FC),
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${dailyPulse.youthStudyCirclesActiveCount} Active Circles • ${dailyPulse.examPeerAspirantsConnected} Aspirants Connected",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Tele-MANAS Counseling: 24x7 Active (Toll Free 14416)",
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
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                        border = BorderStroke(1.dp, Color(0xFFE9D5FF))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "💚", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଟେଲି-ମାନସ ମାଗଣା ପରାମର୍ଶ (Tele-MANAS)" else "Tele-MANAS 24x7 Confidential Counseling",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate900)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "ଓଡ଼ିଶା ସରକାରଙ୍କ ଦ୍ୱାରା ପରିଚାଳିତ ଟେଲି-ମାନସ (୧୪୪୧୬) ମାଧ୍ୟମରେ ପ୍ରତିଯୋଗିତା ପରୀକ୍ଷା ଚାପ, ଅବସାଦ ଓ ମାନସିକ ସ୍ୱାସ୍ଥ୍ୟ ପାଇଁ ଓଡ଼ିଆ ଭାଷାରେ ମାଗଣା ପରାମର୍ଶ ଉପଲବ୍ଧ।"
                                else
                                    "Govt of Odisha Tele-MANAS (14416) provides free, 100% confidential psychological support and exam-stress counseling in Odia and English. Accessible 24 hours a day.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate700, fontSize = 12.sp)
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଓପିଏସସି, ୟୁପିଏସସି ଓ ପୋଲିସ ପରୀକ୍ଷା ଅଧ୍ୟୟନ ସର୍କଲ" else "Competitive Exam Peer Study Circle",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Daily GK quiz, Odisha current affairs, and mentorship matching at Fakir Mohan University library and Gandhi Smruti Bhawan reading rooms.",
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
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:14416"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_call_tele_manas"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Tele-MANAS (14416)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Balasore Youth Wellness Hub")
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Need exam stress support or study circle in Balasore? Call Tele-MANAS at 14416 (Toll-Free, 24x7). Download Balasore 360 app for student study circles."
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Youth Desk"))
                            },
                            modifier = Modifier.weight(1f).testTag("btn_share_wellness_desk"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Share Desk", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}
