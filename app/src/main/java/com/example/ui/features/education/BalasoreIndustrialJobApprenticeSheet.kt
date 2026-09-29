package com.example.ui.features.education

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import kotlinx.coroutines.delay

@Composable
fun BalasoreIndustrialJobApprenticeSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(60) }
    var selectedFilter by remember { mutableStateOf("All") } // "All", "ITI / Diploma", "Graduate", "Apprentice"

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 60
            }
        }
    }

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by pulseTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val jobListings = listOf(
        IndustrialOpening(
            companyName = "Emami Paper Mills Ltd.",
            estate = "Chhanpur Industrial Zone",
            roleEn = "Graduate Engineering Trainee & ITI Apprentice",
            roleOd = "ଗ୍ରାଜୁଏଟ୍ ଇଞ୍ଜିନିୟରିଂ ଓ ଆଇଟିଆଇ ଆପ୍ରେଣ୍ଟିସ୍",
            qualification = "Diploma / ITI (Fitter, Electrical)",
            stipend = "₹14,500 / month + Bus Facility",
            vacancies = 18,
            type = "Apprentice",
            deadline = "15 Oct 2026",
            contactPhone = "06782275723"
        ),
        IndustrialOpening(
            companyName = "Balasore Alloys Limited",
            estate = "Balgopalpur Industrial Estate",
            roleEn = "Furnace Plant Operator & Safety Supervisor",
            roleOd = "ପ୍ଲାଣ୍ଟ ଅପରେଟର ଓ ସୁରକ୍ଷା ସୁପରଭାଇଜର",
            qualification = "B.Tech / Diploma Metallurgy/Mechanical",
            stipend = "₹22,000 - ₹28,000 / month",
            vacancies = 8,
            type = "Graduate",
            deadline = "20 Oct 2026",
            contactPhone = "06782275601"
        ),
        IndustrialOpening(
            companyName = "Falcon Marine Exports Park",
            estate = "Kuruda Industrial Area",
            roleEn = "Cold Storage Quality Analyst & Packaging",
            roleOd = "କୋଲ୍ଡ ଷ୍ଟୋରେଜ୍ କ୍ୱାଲିଟି ପରୀକ୍ଷକ",
            qualification = "B.Sc Chemistry / Food Tech / +2 Science",
            stipend = "₹16,000 / month",
            vacancies = 12,
            type = "Graduate",
            deadline = "18 Oct 2026",
            contactPhone = "06782256190"
        ),
        IndustrialOpening(
            companyName = "Somnathpur Engineering Cluster",
            estate = "Somnathpur IDCO Estate",
            roleEn = "CNC Machinist & Welder Apprentice (NATS)",
            roleOd = "ସିଏନସି ମେସିନିଷ୍ଟ ଓ ୱେଲଡର ଆପ୍ରେଣ୍ଟିସ୍",
            qualification = "ITI Welder / Machinist / Turner",
            stipend = "₹12,000 / month (Govt Stipend)",
            vacancies = 25,
            type = "ITI / Diploma",
            deadline = "25 Oct 2026",
            contactPhone = "06782240120"
        )
    )

    val filteredListings = if (selectedFilter == "All") {
        jobListings
    } else {
        jobListings.filter { it.type == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏭", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ବାଲେଶ୍ୱର ଶିଳ୍ପାଞ୍ଚଳ ନିଯୁକ୍ତି ଓ ଆପ୍ରେଣ୍ଟିସ୍" else "Balasore Industrial Job Board",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4338CA)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ସୋମନାଥପୁର, କୁରୁଡ଼ା ଓ ଛାନପୁର କମ୍ପାନୀ ଯାଞ୍ଚକୃତ ଚାକିରି ସୂଚୀ" else "Verified Openings across Somnathpur, Kuruda & Chhanpur",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFEEF2FF),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4F46E5).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4338CA)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Metric Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
            border = BorderStroke(1.dp, Color(0xFFC7D2FE))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdia) "ଜିଲ୍ଲା ନିଯୋଜନ ଓ IDCO ଶିଳ୍ପାଞ୍ଚଳ ପଲ୍ସ" else "District Employment & IDCO Pulse",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF312E81)
                        )
                    )
                    Surface(
                        color = Color(0xFF4F46E5),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "63 OPENINGS",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    JobMetricBox(
                        label = if (isOdia) "ଆପ୍ରେଣ୍ଟିସ୍ ସିଟ୍" else "Apprentice Seats",
                        value = "43 Active",
                        sub = if (isOdia) "NATS / NAPS ସ୍ୱୀକୃତ" else "NATS Portal",
                        color = Color(0xFF4338CA)
                    )
                    JobMetricBox(
                        label = if (isOdia) "ସ୍ଥାୟୀ ନିଯୁକ୍ତି" else "Full-Time Jobs",
                        value = "20 Openings",
                        sub = if (isOdia) "ଇଞ୍ଜିନିୟରିଂ ଓ ପ୍ଲାଣ୍ଟ" else "Core Industry",
                        color = Color(0xFF047857)
                    )
                    JobMetricBox(
                        label = if (isOdia) "ମଡେଲ୍ କ୍ୟାରିୟର ସେଣ୍ଟର" else "MCC Balasore",
                        value = "Free Registration",
                        sub = if (isOdia) "ଜିଲ୍ଲା କାର୍ଯ୍ୟାଳୟ" else "Station Road",
                        color = Color(0xFFD97706)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "ITI / Diploma", "Graduate", "Apprentice").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(text = filter, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF4F46E5),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Listings
        filteredListings.forEach { job ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = job.companyName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = job.estate,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                        }
                        Surface(
                            color = Color(0xFFEEF2FF),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${job.vacancies} Seats",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4338CA)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isOdia) job.roleOd else job.roleEn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Req: ${job.qualification}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0F766E))
                        )
                        Text(
                            text = job.stipend,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${job.contactPhone}"))
                                context.startActivity(callIntent)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isOdia) "HR ସହ କଥା ହୁଅନ୍ତୁ" else "Call HR", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val portalIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://empmission.odisha.gov.in/"))
                    context.startActivity(portalIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ନିଯୋଜନ ପୋର୍ଟାଲ" else "NAPS Portal")
            }

            OutlinedButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Balasore Industrial Job Board: Verified openings at Emami Paper, Balasore Alloys, Falcon Marine. Apply on Balasore 360 App."
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Jobs"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ସେୟାର୍" else "Share")
            }
        }
    }
}

@Composable
fun JobMetricBox(label: String, value: String, sub: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Text(
            text = sub,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF94A3B8))
        )
    }
}

private data class IndustrialOpening(
    val companyName: String,
    val estate: String,
    val roleEn: String,
    val roleOd: String,
    val qualification: String,
    val stipend: String,
    val vacancies: Int,
    val type: String,
    val deadline: String,
    val contactPhone: String
)
