package com.example.ui.features.education

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakirMohanStudentHubSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val notices = listOf(
        CampusNoticeItem(
            title = "FMU PG Semester Examination Schedule",
            issuer = "Controller of Examinations, FMU",
            date = "Oct 2026",
            details = "Admit cards published for M.Sc, M.Com, MA regular & back paper candidates on student portal."
        ),
        CampusNoticeItem(
            title = "Medhabruti & Post-Matric Scholarship Deadline",
            issuer = "State Scholarship Portal (Govt of Odisha)",
            date = "Active Window",
            details = "Eligible students must verify Aadhaar linking with bank accounts before submission deadline."
        ),
        CampusNoticeItem(
            title = "OSSSC Competitive Recruitment Exam Mock Center",
            issuer = "District Employment Exchange, Balasore",
            date = "Every Saturday",
            details = "Free mock tests and computer skill training conducted at Balasore ITI & FM College Hall."
        ),
        CampusNoticeItem(
            title = "Central University & Railway RRB Balasore Test Centers",
            issuer = "Nodal Education Officer",
            date = "Registration Open",
            details = "Biometric and hall guidelines issued for Balasore, Remuna, and Soro examination centers."
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ ଓ ଛାତ୍ର ହବ୍ 🎓" else "Fakir Mohan University Campus Hub 🎓",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପରୀକ୍ଷା ସୂଚୀ, ମେଧାବୃତ୍ତି, ପାଠ୍ୟପୁସ୍ତକ ଓ ପ୍ରତିଯୋଗିତା ପରୀକ୍ଷା ଆଲର୍ଟ" else "Exam Schedules, Scholarships & Student Career Feed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Campus Bulletin
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("FMU NOTIFICATION RADAR", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                Text("${dailyPulse.fmuUpcomingEventsCount} ALERTS", color = Color.White, fontSize = 10.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(dailyPulse.fmuCampusNotice, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            // Quick Links
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://fmuniversity.nic.in"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("FMU Portal")
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://scholarship.odisha.gov.in"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Scholarships 📜")
                    }
                }
            }

            // Notice Cards
            items(notices) { notice ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(notice.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Badge(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                                Text(notice.date, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(notice.details, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Issued by: ${notice.issuer}", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class CampusNoticeItem(
    val title: String,
    val issuer: String,
    val date: String,
    val details: String
)
