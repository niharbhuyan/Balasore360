package com.example.ui.features.civic

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CastForEducation
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/**
 * Balasore Student Career Radar & District Library Seat Pulse.
 * Real-time reading room seat tracker for Fakir Mohan Bhasha Bhawan,
 * Odisha scholarship eligibility directory, and competitive exam bulletins.
 */
@Composable
fun StudentCareerLibraryPulseSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val seatFraction = (dailyPulse.districtLibrarySeatsOccupied.toFloat() / dailyPulse.districtLibraryTotalSeats.toFloat()).coerceIn(0f, 1f)

    val scholarships = listOf(
        Pair("e-Medhabruti (Higher Education)", "Merit-based financial grant for Degree, PG & Technical students in Balasore"),
        Pair("KALIA Chhatra Brutti Scheme", "Full educational expense waiver for children of Balasore farmers pursuing professional courses"),
        Pair("Post-Matric ST/SC & OBC Scholarship", "Monthly maintenance allowance and tuition reimbursement via Odisha State Scholarship Portal")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("student_career_library_pulse_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF6366F1).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF6366F1)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎓", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଛାତ୍ର ବୃତ୍ତି ଓ ପାଠାଗାର ସିଟ୍ ପଲ୍ସ" else "Student Career & Library Pulse",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Fakir Mohan University • District Central Library",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF4F46E5),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF10B981))
            ) {
                Text(
                    text = "AUTO-SYNC 🟢",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // District Library Live Seat Occupancy Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
            border = BorderStroke(1.5.dp, Color(0xFF818CF8))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "District Central Library Reading Room",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3730A3)
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF4338CA)
                    ) {
                        Text(
                            text = "${dailyPulse.districtLibrarySeatsOccupied} / ${dailyPulse.districtLibraryTotalSeats} SEATS",
                            color = Color.White,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { seatFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (seatFraction > 0.85f) Color(0xFFEF4444) else Color(0xFF4F46E5),
                    trackColor = Color(0xFFC7D2FE)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "High-speed Wi-Fi, air-conditioned study hall, and competitive reference books. Open 08:00 AM - 08:00 PM.",
                    fontSize = 11.5.sp,
                    color = Color(0xFF4338CA)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upcoming Local Recruitment & Apprentice Drives
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Work, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Featured Balasore Career Bulletin", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(dailyPulse.upcomingApprenticeDrive, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                Text("South Eastern Railway Division • Electric Loco Shed & Workshop Training", fontSize = 11.sp, color = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // State Scholarship Directory
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Odisha State Scholarships Active (${dailyPulse.activeStateScholarshipsCount} Portals)", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                scholarships.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(item.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF312E81))
                            Text(item.second, fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done / Close Career Radar", fontWeight = FontWeight.Bold)
        }
    }
}
