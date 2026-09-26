package com.example.ui.features.heritage

import android.media.ToneGenerator
import android.media.AudioManager
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreTemplePilgrimageCompanionSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    var selectedTempleIndex by remember { mutableIntStateOf(0) }
    var isBellRinging by remember { mutableStateOf(false) }

    val temples = listOf(
        TempleDetail(
            nameEn = "Remuna Khirachora Gopinath Temple",
            nameOd = "ରେମୁଣା ଖିରଚୋରା ଗୋପୀନାଥ ଜିଉ",
            location = "Remuna Block, 9 km from Balasore",
            deity = "Lord Krishna (Stealer of Condensed Milk / Khira)",
            specialty = "Holy Amruta Keli Khira Bhog in earthen pots",
            timings = "05:30 AM - 12:45 PM & 04:30 PM - 08:30 PM",
            currentPhase = dailyPulse.remunaActiveAartiPhase,
            potsRemaining = dailyPulse.remunaKhiraBhogPotsAvailable,
            history = "Saint Madhavendra Puri received the miraculously hidden bowl of condensed milk directly from Lord Gopinath in the 15th century.",
            rituals = listOf(
                "05:30 AM • Mangala Alati (First Darshan)",
                "08:00 AM • Sakala Dhupa & Tulasi Arpan",
                "12:30 PM • Madhyanna Dhupa & Special Khira Bhog Offering",
                "06:45 PM • Sandhya Deepa Alati & Gita Govinda recitation",
                "08:30 PM • Pahada (Night rest of the Lord)"
            )
        ),
        TempleDetail(
            nameEn = "Panchalingeswar Temple",
            nameOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ମହାଦେବ",
            location = "Nilagiri Hills, 30 km from Balasore",
            deity = "Five Swayambhu Shiva Lingams submerged in mountain spring",
            specialty = "Touch the holy lingams inside perennial hill stream",
            timings = "06:00 AM - 06:30 PM (Daylight hours)",
            currentPhase = if (dailyPulse.isPanchalingeswarClimbSafe) "Climb Safe 🟢 (Spring Flow: ${dailyPulse.panchalingeswarStreamFlowMeters}m)" else "Caution on Steps ⚠️",
            potsRemaining = null,
            history = "Revered in Valmiki Ramayana; Goddess Sita worshiped these five self-manifested Shiva lingams in the Nilagiri hills during her exile.",
            rituals = listOf(
                "06:00 AM • Morning Spring Jalabhisheka",
                "11:30 AM • Hill Stream Maha Naivedya",
                "05:30 PM • Sunset Alati & Mountain Twilight Gate Closing"
            )
        ),
        TempleDetail(
            nameEn = "Chandaneswar Shiva Temple",
            nameOd = "ଚନ୍ଦନେଶ୍ୱର ମହାଦେବ",
            location = "Bhograi Block, Coastal Border",
            deity = "Lord Shiva (Chandaneswar Bada Deula)",
            specialty = "Ancient miracle temple famous for historic Chadak Mela",
            timings = "05:00 AM - 01:00 PM & 04:00 PM - 09:00 PM",
            currentPhase = "Holy Darshan & Bilwa Patra Arpan Active",
            potsRemaining = null,
            history = "Renowned regional pilgrimage center where millions assemble during the Chaitra Pana Sankranti for holy penance and fire-walking rites.",
            rituals = listOf(
                "05:00 AM • Pahali Dhupa & Gangajal Snana",
                "12:00 PM • Madhyanna Bhoga (Sweet Rice & Dal)",
                "07:00 PM • Sandhya Sankha Dhwani & Alati"
            )
        ),
        TempleDetail(
            nameEn = "Baba Bhusandheswar Temple",
            nameOd = "ବାବା ଭୂଷଣ୍ଡେଶ୍ୱର ମନ୍ଦିର",
            location = "Bhusandheswar, near Subarnarekha bank",
            deity = "Colossal Monolithic Shiva Lingam (Asia's Largest)",
            specialty = "Black granite monolithic lingam (12ft height, 14ft circumference)",
            timings = "06:00 AM - 07:30 PM",
            currentPhase = "Dugdha & Belpatra Abhisheka Open",
            potsRemaining = null,
            history = "Legends date back to Treta Yuga when Ravana carried this mighty Shiva lingam to Lanka and placed it down near the Subarnarekha river bank.",
            rituals = listOf(
                "06:30 AM • Pratah Maha Abhisheka with Pure Milk",
                "01:00 PM • Anna Bhoga & Chhatra Arpan",
                "06:30 PM • Deepa Dāna & Sandhya Alati"
            )
        )
    )

    val selectedTemple = temples[selectedTempleIndex]

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
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମନ୍ଦିର ଆଳତି ଓ ଖିରଭୋଗ 🛕" else "Temple Aarti & Khira Bhog Companion 🛕",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରେମୁଣା, ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର, ଚନ୍ଦନେଶ୍ୱର ଓ ଭୂଷଣ୍ଡେଶ୍ୱର ଲାଇଭ୍ ଦର୍ଶନ ସମୟ" else "Live Gate Timings, Khira Bhog Availability & Mountain Springs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Spiritual Bell Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF78350F)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("TEMPLE CHIME & SHANKHA", color = Color(0xFFFDE68A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Play Sacred Bell Sound", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Audible darshan chime for prayer meditation", color = Color(0xFFFCD34D), fontSize = 11.sp)
                        }
                        IconButton(
                            onClick = {
                                try {
                                    val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
                                    toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 400)
                                } catch (_: Exception) {}
                                isBellRinging = true
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B))
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = "Ring Temple Bell", tint = Color(0xFF78350F))
                        }
                    }
                }
            }

            // Remuna Khira Bhog Live Counter
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🥣", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Remuna Khira Bhog Counter", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF78350F))
                                    Text("Fresh earthen pots prepared today", fontSize = 11.sp, color = Color(0xFF92400E))
                                }
                            }
                            Badge(containerColor = Color(0xFFB45309)) {
                                Text("${dailyPulse.remunaKhiraBhogPotsAvailable} POTS READY", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(2.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Current Sanctum Phase: ${dailyPulse.remunaActiveAartiPhase}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB45309)
                        )
                    }
                }
            }

            // Temple Selector Tabs
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପବିତ୍ର ତୀର୍ଥ କ୍ଷେତ୍ର" else "Select Sacred Sanctum",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    temples.forEachIndexed { index, temple ->
                        val isSelected = index == selectedTempleIndex
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTempleIndex = index },
                            label = {
                                Text(
                                    text = if (index == 0) "Remuna" else if (index == 1) "Panchalingeswar" else if (index == 2) "Chandaneswar" else "Bhusandheswar",
                                    fontSize = 11.sp
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Selected Temple Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) selectedTemple.nameOd else selectedTemple.nameEn,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(selectedTemple.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Deity: ${selectedTemple.deity}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("Specialty: ${selectedTemple.specialty}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Gate Hours: ${selectedTemple.timings}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("🔔 ${selectedTemple.currentPhase}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Daily Aarti & Ritual Schedule", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        selectedTemple.rituals.forEach { ritual ->
                            Text("• $ritual", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 2.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = selectedTemple.history,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class TempleDetail(
    val nameEn: String,
    val nameOd: String,
    val location: String,
    val deity: String,
    val specialty: String,
    val timings: String,
    val currentPhase: String,
    val potsRemaining: Int?,
    val history: String,
    val rituals: List<String>
)
