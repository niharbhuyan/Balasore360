package com.example.ui.features.health

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

data class MobileMedicalVanRoute(
    val vanId: String,
    val unitNameEn: String,
    val unitNameOd: String,
    val targetBlock: String,
    val todayCampVillage: String,
    val doctorInCharge: String,
    val servicesOffered: String,
    val diagnosticTestsFree: String,
    val timing: String,
    val helplineNumber: String
)

val DEFAULT_MMU_ROUTES = listOf(
    MobileMedicalVanRoute(
        vanId = "mmu_1",
        unitNameEn = "MMU Unit 01 - Nilagiri Tribal Corridor",
        unitNameOd = "ମୋବାଇଲ୍ ମେଡିକାଲ୍ ୟୁନିଟ୍ ୦୧ - ନୀଳଗିରି ଆଦିବାସୀ ଅଞ୍ଚଳ",
        targetBlock = "Nilagiri",
        todayCampVillage = "Badasahi GP Community Hall",
        doctorInCharge = "Dr. S. Mohanty (MBBS, DNB Family Med)",
        servicesOffered = "General OPD, NCD Screening, Maternal Care, ECG",
        diagnosticTestsFree = "Blood Sugar, Hb, Malaria RDT, Blood Grouping",
        timing = "09:30 AM - 02:30 PM",
        helplineNumber = "+91 6782 262201"
    ),
    MobileMedicalVanRoute(
        vanId = "mmu_2",
        unitNameEn = "MMU Unit 02 - Oupada Hill Reserve",
        unitNameOd = "ମୋବାଇଲ୍ ମେଡିକାଲ୍ ୟୁନିଟ୍ ୦୨ - ଔପଦା ପାହାଡ଼ିଆ ଅଞ୍ଚଳ",
        targetBlock = "Oupada",
        todayCampVillage = "Fatepur Primary School Camp",
        doctorInCharge = "Dr. R. Panda (MBBS, Child Health)",
        servicesOffered = "Pediatric health, Immunization, Deworming, Malnutrition Care",
        diagnosticTestsFree = "Hemoglobin, Rapid Typhoid, Sickle Cell Screening",
        timing = "10:00 AM - 03:00 PM",
        helplineNumber = "+91 6782 262202"
    ),
    MobileMedicalVanRoute(
        vanId = "mmu_3",
        unitNameEn = "MMU Unit 03 - Baliapal Coastal Fisherfolk",
        unitNameOd = "ମୋବାଇଲ୍ ମେଡିକାଲ୍ ୟୁନିଟ୍ ୦୩ - ବାଲିଆପାଳ ଉପକୂଳ ମତ୍ସ୍ୟଜୀବୀ",
        targetBlock = "Baliapal",
        todayCampVillage = "Dagara Cyclone Shelter Health Camp",
        doctorInCharge = "Dr. P. Sethi (MBBS, Dermatology & Infection)",
        servicesOffered = "Water-borne disease check, Skin allergy, Geriatric arthritis",
        diagnosticTestsFree = "VDRL, Stool test, Blood pressure, Urine routine",
        timing = "09:00 AM - 02:00 PM",
        helplineNumber = "+91 6782 262203"
    ),
    MobileMedicalVanRoute(
        vanId = "mmu_4",
        unitNameEn = "MMU Unit 04 - Bhograi Subarnarekha Border",
        unitNameOd = "ମୋବାଇଲ୍ ମେଡିକାଲ୍ ୟୁନିଟ୍ ୦୪ - ଭୋଗରାଇ ସୀମାନ୍ତ ଅଞ୍ଚଳ",
        targetBlock = "Bhograi",
        todayCampVillage = "Kusuda GP Mandap",
        doctorInCharge = "Dr. K. N. Das (MBBS, Medicine)",
        servicesOffered = "Hypertension, Diabetes, Anti-venom availability check",
        diagnosticTestsFree = "Lipid profile sample collection, Blood glucose, BP",
        timing = "09:30 AM - 02:30 PM",
        helplineNumber = "+91 6782 262204"
    )
)

/**
 * Balasore Rural Mobile Medical Units (MMU) & Jan Aushadhi Telemedicine Radar.
 */
@Composable
fun RuralMobileTelemedicineNetworkSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var lastRefreshed by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("rural_telemedicine_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), Color(0xFF047857), EmeraldGreen)
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
                            color = Color(0xFF34D399).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "NATIONAL HEALTH MISSION • 12 MMU VANS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFFA7F3D0)
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
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଗ୍ରାମୀଣ ମୋବାଇଲ୍ ମେଡିକାଲ୍ ଭ୍ୟାନ୍ ଓ ଔଷଧ ସେବା" else "Balasore Rural Mobile Medical Network",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ନୀଳଗିରି, ଔପଦା ଓ ଉପକୂଳ ଗ୍ରାମାଞ୍ଚଳରେ ଦୈନିକ ଡାକ୍ତରୀ କ୍ୟାମ୍ପ, ମାଗଣା ପରୀକ୍ଷା ଓ ପ୍ରଧାନମନ୍ତ୍ରୀ ଜନ ଔଷଧି କେନ୍ଦ୍ର ଟେଲିମେଟ୍ରି"
                        else
                            "Daily village schedule for 12 Mobile Medical Units, free diagnostic tests, and Jan Aushadhi generic medicines.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Status Pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dailyPulse.todayOnDutyEmergencyDoctors,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { lastRefreshed = System.currentTimeMillis() },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Jan Aushadhi Discount Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.LocalPharmacy, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Pradhan Mantri Jan Aushadhi Kendra (PMBJP)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = Color(0xFF166534)
                        )
                        Text(
                            text = "50% to 90% savings on top 450 essential medicines at DHH Balasore, Remuna & Jaleswar branches.",
                            fontSize = 11.sp,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }
        }

        items(DEFAULT_MMU_ROUTES) { route ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
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
                                text = if (language == AppLanguage.ODIA) route.unitNameOd else route.unitNameEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "Camp Location: ${route.todayCampVillage}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BentoSlate100,
                            border = BorderStroke(1.dp, BentoSlate200)
                        ) {
                            Text(
                                text = route.timing,
                                color = BentoSlate800,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BentoSlate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Physician: ${route.doctorInCharge}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BentoSlate900
                    )
                    Text(
                        text = "Free Diagnostics: ${route.diagnosticTestsFree}",
                        fontSize = 11.sp,
                        color = BentoSlate700
                    )
                    Text(
                        text = "Scope: ${route.servicesOffered}",
                        fontSize = 11.sp,
                        color = BentoSlate600
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${route.helplineNumber}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Health Camp Coordinator", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
